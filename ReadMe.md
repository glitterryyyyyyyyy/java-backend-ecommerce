# Java Backend E-commerce App

This is a mini project for the Nebula Technologies internship.

## Features
- Secure Login & Signup with OTP verification
- Greeting email after signup
- Admin dashboard for user and product management
- Product listing with add to cart functionality

## Tech Stack
- Java
- Spring Boot
- Spring Security
- H2 Database
- JWT Authentication
- Email Service (SMTP)
- Render (for backend hosting)

## How to Run Locally
1. Clone this repo
2. Open the project in your IDE
3. Make sure you have Java and Maven installed
4. Run the application
5. Use Postman/Thunder Client to test APIs

## API Testing
- Base URL: http://localhost:8081
- Authentication endpoints:  
  /api/user/signup, /api/user/verify, /api/user/login

- Product endpoints:  
  /api/products (GET), /api/admin/products (admin only)

- Cart endpoints:  
  /api/cart/add (POST), /api/cart (GET)
