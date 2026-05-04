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

- Layered architecture (API Client, Business Logic, Tests)
- Full CRUD coverage (Create, Read, Update, Delete)
- Positive and negative test scenarios (validation, 404 handling)
- Automated data cleanup after test execution
- Builder pattern for flexible test data generation
- Contract tests to verify specific API behaviors

## How to Run

```bash
mvn clean test
