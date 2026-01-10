# EventSphere 

## 1. System Overview

EventSphere is a platform designed to manage and analyze events such as conferences, workshops, and festivals. The system allows organizers to create events and sessions, users to register and participate, and administrators to analyze participation, feedback, and performance metrics. The application focuses not only on operational management but also on analytics and insights, making it suitable for a master-level project.

---

## 2. Business Domain

The business domain of the application is **event organization and participation management**, combined with **data analytics** regarding attendance, session popularity, and user feedback.

---

## 3. Business Requirements (10)

1. The system must allow organizers to create, update, and delete events.
2. Each event must support multiple sessions scheduled at specific times.
3. Users must be able to register for events.
4. Users must be able to enroll in individual sessions of an event.
5. The system must manage speakers assigned to sessions.
6. The system must prevent registrations or enrollments when capacity limits are exceeded.
7. Participants must be able to leave feedback and ratings for sessions they attended.
8. The system must calculate and expose the occupancy rate of events.
9. The system must identify sessions with low attendance.
10. All data must be persisted in a relational database and support analytical queries.

---

## 4. MVP – Minimum Viable Product Features (5)

### 4.1 Event Management

Organizers can create and manage events, including details such as title, location, period, and maximum capacity.

### 4.2 Session Management

Each event can contain multiple sessions, each having a defined schedule, speaker, and capacity.

### 4.3 Registration & Enrollment

Users can register for events and enroll in specific sessions, with validation to ensure capacity constraints are respected.

### 4.4 Feedback & Rating System

Participants can submit feedback and ratings for sessions they attended, enabling quality assessment.

### 4.5 Analytics & Reporting

The system provides analytical insights such as event occupancy rate, most popular sessions, and sessions with low attendance.

---

## 5. MVP Scope Notes

* Authentication and authorization are kept minimal for the MVP.
* The focus is on backend functionality and analytics rather than UI complexity.
* The MVP is designed to be extensible for future enhancements.

---

## 6. Conclusion

This document defines the foundation of the EventSphere project. It serves as the reference point for implementation decisions and ensures alignment with the project requirements and evaluation criteria.
