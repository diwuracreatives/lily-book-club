# 🌸 LILY BOOK CLUB API

**LILY BOOK CLUB** is a Spring Boot application that powers a community-driven book recommendation platform. Users receive **weekly emails** with book recommendations tailored to their interests and skills, join club groups, vote on books, and manage their membership.

---

##  Project Definition
Send weekly emails to users with recommended books based on their interests and skill level. Users can join interest-based club groups, receive reminders, vote or recommend books, and opt out anytime.

---

## Features
- **User Accounts & Authentication** – Sign up, login, and manage profiles.
- **Club Management** – Join or leave interest-based book clubs.
- **Weekly Book Recommendations** – Curated emails delivered to members.
- **Voting & Recommendations** – Suggest and vote on books in your club.
- **Email Notifications** – Reminders and updates about new book recommendations.

---

##  Tech Stack
- **Backend:** Spring Boot
- **Database:** MySQL
- **Email Service:** JavaMail / SendGrid
- **Authentication:** Spring Security / JWT
- **API Documentation:** Swagger UI / Postman

---

##  Installation & Setup
1. **Clone the repository**
```bash
git clone https://github.com/diwuracreatives/lily-book-club.git
cd lily-book-club
```
2. **Configure environment variables**
Edit Application.properties file

3. **Build and Run the App**
```bash
spring-boot:run
```