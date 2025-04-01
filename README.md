# Healthcare Management System

![Java](https://img.shields.io/badge/Java-17%2B-blue)
![OOP](https://img.shields.io/badge/OOP-Yes-success)
![File-Based](https://img.shields.io/badge/Storage-File%20Based-yellow)

A Java-based healthcare management system for scheduling appointments, managing patient records, and handling doctor-patient interactions.

## Features

### User Roles
- **Admin**: Add doctors, view all users/appointments
- **Doctor**: Manage appointments, create medical records
- **Patient**: Book appointments, view medical history

### Core Functionalities
- 📅 Appointment booking system
- 🏥 Doctor specialization management
- 📝 Digital medical records
- 🔐 Role-based authentication
- 📊 Data persistence (text files)

## Class Diagram
```mermaid
classDiagram
    class User {
        +String username
        +String password
        +String name
    }
    User <|-- Admin
    User <|-- Doctor
    User <|-- Patient
    class Appointment {
        +bookAppointment()
    }
    Appointment --> Doctor
    Appointment --> Patient
Installation
Requirements:

Java 17+

Maven (for building)

Run the system:
git clone https://github.com/your-repo/healthcare-system.git
cd healthcare-system
java HealthCareSystem.java

File Structure
healthcare-system/
├── data/               # Storage files
│   ├── users.txt
│   ├── appointments.txt
│   └── records.txt
├── src/
│   ├── HealthCareSystem.java  # Main class
│   ├── User.java              # Base user class
│   ├── Admin.java             # Admin functions
│   ├── Doctor.java            # Doctor functions
│   ├── Patient.java           # Patient functions
│   ├── Appointment.java       # Appointment logic
│   └── MedicalRecord.java     # Medical records
└── README.md

Usage Examples
As Admin
1. Add Doctor
2. View All Doctors
3. View All Patients
4. View All Appointments
As Doctor
1. View My Appointments
2. Create Medical Record
3. View Patient Records
As Patient
1. Book Appointment
2. View Medical Records
3. Check Appointment Status
Data Storage Format
users.txt:

TYPE|USERNAME|PASSWORD|NAME|SPECIALIZATION|FEES|DOB|PHONE
DOCTOR|drsmith|smith123|Dr. Smith|Cardiology|150
PATIENT|john|john123|John Doe|1990-01-01|123-456-7890


Contributing
Fork the project

Create your feature branch (git checkout -b feature/fooBar)

Commit your changes (git commit -am 'Add some fooBar')

Push to the branch (git push origin feature/fooBar)

Open a Pull Request


License
MIT © [Samar Mittal]

---

### Key Sections Included:
1. **Badges** - Quick tech stack overview
2. **Visual Diagrams** - MermaidJS class diagram
3. **File Structure** - Clear project organization
4. **Usage Examples** - Sample menu flows
5. **Data Formats** - Shows how data is stored
6. **Contributing Guide** - Standard GitHub workflow

To use this:
1. Copy the entire content
2. Paste into a new `README.md` file in your project root
3. Update the repository link and license info
4. Commit to GitHub

Would you like me to add any specific sections like:
- Screenshots of console output?
- Detailed setup instructions for beginners?
- Error handling documentation?
