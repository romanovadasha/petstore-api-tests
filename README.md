# Petstore API Tests

REST API test automation framework for Petstore built with Java, RestAssured and JUnit.

## Overview

This project demonstrates API test automation for Petstore service, including:

- CRUD operations testing
- Request/response validation
- Data modeling and test data generation
- Layered test architecture

  Includes both positive and negative test scenarios for API validation.

## Tech Stack

- Java
- RestAssured
- JUnit 5
- Maven

## Project Structure

- `api` — API client for sending requests
- `models` — request/response models (Pet, PetBuilder)
- `service` — business logic layer (PetService)
- `config` — request/response specifications
- `tests` — test cases
- `base` — base test setup

## Features

- Separation of concerns (client / service / tests)
- Reusable request and response specifications
- Builder pattern for test data creation
- Clean and maintainable test structure

## How to Run

```bash
mvn clean test
