.PHONY: help
.DEFAULT_GOAL := help

help:
	python -m n2t --help

install: ## Install requirements
	python -m pip install --upgrade pip
	python -m pip install --upgrade poetry
	poetry install --no-root

lock: ## Lock project dependencies
	poetry lock --no-update

update: ## Update project dependencies
	poetry update

format: ## Run code formatters
	poetry run ruff format n2t tests
	poetry run ruff check  n2t tests --fix

lint: ## Run code linters
	poetry run ruff format n2t tests --check
	poetry run ruff check  n2t tests
	poetry run mypy n2t tests

test:  ## Run tests with coverage
	poetry run pytest --cov --hypothesis-profile easy
