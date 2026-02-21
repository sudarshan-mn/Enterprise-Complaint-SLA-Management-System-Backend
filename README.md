Complaint & SLA Management System – Backend

This is a backend-focused complaint management system built using Spring Boot.

The idea behind this project was to simulate how real-world platforms (telecom, banking, e-commerce support systems) handle customer complaints with proper workflows, role-based access, and SLA tracking.

🌐 Live Demo:
https://complaint-sla-management.netlify.app/login

Note: This project mainly focuses on backend architecture and business logic. The frontend is only for demonstration.

What this project does

In simple terms, this system allows different roles to manage complaints end-to-end.

Customers can raise complaints and track their status

Admin assigns complaints to engineers

Engineers work on and resolve them

Leads can reassign or escalate issues

Managers monitor SLA breaches and close complaints

Every important action is recorded for traceability.

Roles in the system

Each role has strict access control using Spring Security.

CUSTOMER – Raise and track complaints

ADMIN – Assign complaints and monitor dashboards

ENGINEER – Work on assigned complaints

LEAD – Reassign or escalate complaints

MANAGER – Monitor SLA breaches and close issues

Complaint lifecycle

The complaint flow follows a controlled lifecycle:

NEW → ASSIGNED → IN_PROGRESS → RESOLVED → CLOSED

Only valid transitions are allowed.
For example, a complaint cannot be resolved before work has started.

SLA handling

One of the key parts of this project is SLA management.

SLA deadlines are calculated on the server side based on priority:

LOW → 72 hours

MEDIUM → 48 hours

HIGH → 24 hours

A scheduler periodically checks for SLA breaches.
If a complaint crosses the deadline:

It is marked as SLA breached

Priority can be escalated automatically

This keeps all business logic centralized and secure.

Complaint history (audit trail)

Every major action is stored as history:

Complaint creation

Assignment

Status updates

Escalations

Resolution and closure

This makes the system audit-friendly and traceable.

Tech stack

Backend

Java 17

Spring Boot

Spring Security (JWT)

Spring Data JPA (Hibernate)

MySQL

Architecture

The project follows a layered architecture:

Controller → Service → Repository

Other highlights:

DTO-based request/response models

Centralized exception handling

Modular package structure

Scheduler for SLA monitoring

Security

JWT-based authentication

Stateless session management

Role-based authorization

Password encryption using PasswordEncoder

Endpoint-level protection

Why I built this

This project was built to practice real backend concepts beyond basic CRUD APIs, including:

Multi-role workflows

SLA enforcement

Audit logging

Secure backend design

The goal was to build something closer to real production systems.

Author

Sudarshan M N
Java Full Stack Developer
Focused on building clean, real-world backend systems.
