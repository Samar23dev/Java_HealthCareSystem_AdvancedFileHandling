# Healthcare Management System

![Java](https://img.shields.io/badge/Java-17%2B-blue)
![OOP](https://img.shields.io/badge/OOP-Yes-success)
![Storage](https://img.shields.io/badge/Storage-Object%20Serialization-purple)

A console-based healthcare management system written in Java, using object serialization for data persistence. Supports role-based access (Admin, Doctor, Patient), appointment scheduling, and medical record management.

## Features

### User Roles
- **Admin**: Add/view doctors, view all patients and appointments.
- **Doctor**: View own appointments, create and view patient medical records.
- **Patient**: Register an account, book appointments, view own profile and medical history.

### Core Functionality
- 🔐 Role-based login and logout flow
- 🏥 Appointment booking and management
- 📝 Medical record creation and viewing
- 💾 Binary data persistence using Java serialization
- 📊 Clean console UI with formatted tables

## Requirements

- Java 17 or later

## Setup & Run

1. **Clone the repository**
   ```bash
   git clone https://github.com/Samar23dev/Java_HealthCareSystem_AdvancedFileHandling.git
   cd JavaFilehandlingHealthCareSystem
   ```
2. **Compile**
   ```bash
   javac *.java
   ```
3. **Run**
   ```bash
   java HealthCareSystem
   ```

On first run, the system initializes the `data/` directory and creates three serialized files with default admin credentials.

## Default Credentials

- **Admin**: `username: admin` / `password: admin123`

You can then add doctors or register patients through the menus.

## Data Storage

All data files are stored in the `data/` folder as serialized objects:

- **users.ser**: List of `User` objects (Admin, Doctor, Patient)
- **appointments.ser**: List of `Appointment` objects
- **records.ser**: List of `MedicalRecord` objects
- **logfile.txt**: Audit log of user actions

> **Note**: The old `.txt` files are no longer used by the system and remain only as backups.

## Project Structure

```
/ (workspace root)
│
├── data/                # persisted serialized files and logs
├── User.java            # base user class (serializable)
├── Admin.java           # admin role class
├── Doctor.java          # doctor role class
├── Patient.java         # patient role class
├── Appointment.java     # appointment model class
├── MedicalRecord.java   # medical record model class
├── ConsoleLogger.java   # simple file logger utility
├── HealthCareSystem.java# main application entrypoint
└── README.md            # this documentation
```

## License

This project is released under the [MIT License](LICENSE).



