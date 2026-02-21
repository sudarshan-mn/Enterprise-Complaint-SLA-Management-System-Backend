Complaint & SLA Management System – Backend

live demo :https://complaint-sla-management.netlify.app/login

This is a role-based complaint management backend built using Spring Boot.
The goal of this project is to simulate how real companies (telecom, banking, e-commerce, support platforms) handle customer complaints with SLA rules, escalation, reassignment, and tracking.

I built this project step by step with real backend workflows in mind, not just CRUD APIs.

What this project does (in simple words)

Customers can raise complaints

Admin assigns complaints to engineers

Engineers work on and resolve complaints

Leads can reassign or escalate complaints

Managers monitor SLA breaches and close complaints

Every important action is recorded as history

SLA deadlines are calculated and monitored automatically

This is how real support systems work.

Roles in the system
Role	What they can do
CUSTOMER	Raise complaints, view own complaints and status
ADMIN	Assign complaints, view dashboard
ENGINEER	Start work, resolve assigned complaints
LEAD	Reassign complaints, escalate priority
MANAGER	View SLA breaches, close resolved complaints

Each role has strict access control using Spring Security.

Complaint flow (lifecycle)
NEW → ASSIGNED → IN_PROGRESS → RESOLVED → CLOSED


Only valid transitions are allowed.
Invalid operations (like resolving without starting work) are blocked.

SLA handling (important part)

SLA deadlines are not sent from frontend.
They are calculated on the server based on priority.

Priority	SLA
LOW	72 hours
MEDIUM	48 hours
HIGH	24 hours

SLA is set when the complaint is created

A scheduler checks for SLA breaches

If SLA is breached:

slaBreached = true

Priority is automatically escalated

This avoids client manipulation and keeps logic centralized.

Complaint History (Audit trail)

Every major action is saved:

Complaint creation

Assignment

Status changes

Reassignment

Escalation

Resolution

Closure

History is stored in a separate table and exposed through a read-only timeline API for internal roles.

This makes the system traceable and audit-friendly.

Tech stack
Backend

Java 17

Spring Boot

Spring Security (JWT)

Spring Data JPA (Hibernate)

MySQL

Lombok

Architecture

REST APIs

DTO-based request/response

Layered architecture (Controller → Service → Repository)

Project structure
src/main/java/com/company/complaintsystem
│
├── controller
├── service
├── repository
├── entity
├── dto
├── security
├── config
├── exception
├── scheduler



Clean separation of responsibilities.

Security

JWT-based authentication

Stateless session management

Role-based authorization using @PreAuthorize

Passwords stored using PasswordEncoder

Endpoints protected per role

Admin dashboard

Admin can see:

Total complaints

Complaints by status

SLA breached complaints

Number of engineers, leads, and managers

This helps in operational monitoring.

Customer features

View own complaints with pagination

View complaint details (status, priority, category, created time)

Track complaint progress

Customers cannot see other users’ complaints.

Scheduler

Periodically checks for SLA breaches

Automatically escalates priority when SLA is missed

Marks complaints as SLA breached

Why this project matters (interview point)

This project shows:

Real business logic

Multi-role workflow

SLA enforcement

Audit logging

Secure backend design




Sudarshan Gowda
Java Full Stack Developer
Focused on building clean, real-world backend systems
