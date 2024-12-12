terraform {
  backend "s3" {
    bucket         = "terraform-backend-fiapeats" # Substitua pelo nome do bucket
    key            = "state/fiapeats-lambda/terraform.tfstate"         # Caminho do estado no bucket
    region         = "us-east-1"                       # Região do bucket
    encrypt        = true                              # Criptografia no bucket
  }
}

provider "aws" {
  region = "us-east-1" # Ajuste para sua região
}

# IAM Role para a função Lambda
resource "aws_iam_role" "lambda_execution_role" {
  name = "lambda_execution_role"

  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Action = "sts:AssumeRole"
        Effect = "Allow"
        Principal = {
          Service = "lambda.amazonaws.com"
        }
      }
    ]
  })
}

# Anexar a política AWSLambdaBasicExecutionRole para permissões de execução
resource "aws_iam_policy_attachment" "lambda_policy_attachment" {
  name       = "lambda_policy_attachment"
  roles      = [aws_iam_role.lambda_execution_role.name]
  policy_arn = "arn:aws:iam::aws:policy/service-role/AWSLambdaBasicExecutionRole"
}

# Função Lambda
resource "aws_lambda_function" "lambda_function" {
  function_name = "lambda_autoriza_cliente"
  role          = aws_iam_role.lambda_execution_role.arn
  handler       = "br.com.fiap.fiapeats.Handler::handleRequest"
  runtime       = "java17"
  timeout       = 120
  memory_size   = 128

  filename         = "../target/fiapeats-lambda-1.0-SNAPSHOT.jar"
  source_code_hash = filebase64sha256("../target/fiapeats-lambda-1.0-SNAPSHOT.jar")

  environment {
    variables = {
      URL_API = "https://3651-191-227-243-148.ngrok-free.app/fiapeats/cliente/"
    }
  }
}

# Permissão para invocar a função Lambda
resource "aws_lambda_permission" "allow_invocation" {
  statement_id  = "AllowExecution"
  action        = "lambda:InvokeFunction"
  function_name = aws_lambda_function.lambda_function.function_name
  principal     = "apigateway.amazonaws.com"
}
