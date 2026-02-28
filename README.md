This project was built as part of my A-Level Computer Science coursework. 
Guy's Hospital Route Finder & Admin System
A Java-based navigation and management system designed to reduce patient anxiety through efficient wayfinding.

📖 Overview
This project was developed as an A-Level Computer Science NEA (Non-Exam Assessment). It addresses a real-world problem faced by patients at Guy's Hospital: navigating the complex layout of the building.

Beyond navigation, the system serves as a comprehensive backend for hospital staff, allowing for the management of patients, doctors, and appointments through a secure, menu-driven interface.

✨ Key Features
1. 🏥 Patient Navigation (The Core Engine)
Shortest Path Calculation: Implements Dijkstra’s Algorithm to calculate the most efficient route from the main entrance to any department.

Time Estimation: Calculates estimated walking time based on distance and average walking speed (1.1 m/s).

Usage Analytics: Tracks the most popular destinations to help hospital administrators understand patient flow.

2. 🔐 Administrative Backend
Role-Based Access Control: Restricted sections (Patient/Doctor maintenance) require secure Admin Login authentication.

CRUD Functionality: Full Create, Read, Update, and Delete capabilities for:

Patients: Manage demographics and records.

Doctors: Manage staff profiles and specialisations.

Appointments: Schedule and reschedule visits with conflict checking.

3. 📊 Feedback & Analytics
Anonymous Feedback Loop: Collects user satisfaction data to drive future improvements.

System Statistics: real-time dashboard displaying total registered users, appointment volume, and system helpfulness ratings.

⚙️ Technical Highlights (Built from Scratch)
Unlike standard implementations that rely heavily on Java's java.util Collections, the core algorithms in this project utilize custom-built data structures to demonstrate a deep understanding of computer science fundamentals:

Custom Priority Queue: Optimized for fetching the nearest node in Dijkstra's algorithm.

Custom Hash Table: Implemented for efficient lookups of visited nodes and distances.

Adjacency List: Built using custom Linked Lists to model the hospital graph.

SQL Integration: Uses SQLite via JDBC for persistent storage of all hospital records.

🛠️ Technology Stack
Language: Java (JDK 17+)

Database: SQLite

Architecture: Modular OOP design (Separation of Models, Database Logic, and UI)

Testing: Unit tested with JUnit (if applicable, otherwise remove this)

🚀 How to Run
Clone the repository:

Bash
git clone https://github.com/badma025/HospitalRouteFinder.git
Open the project in IntelliJ IDEA or Eclipse.

Database Setup: Ensure HospitalDatabase.db is in the root directory.

Run: Execute src/Main.java.

📂 Project Structure
Bash
src/
├── Dijkstra/           # Algorithm logic & custom data structures
├── Models/             # POJOs for Patient, Doctor, Appointment
├── DatabaseConnect.java # SQL Connection Handler
├── Menu.java           # CLI Interface & User Logic
└── Main.java           # Entry Point
Created by Alim Badmus (2025)
