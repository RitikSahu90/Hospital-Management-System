-- =====================================================================
-- schema.sql — Master schema loader
-- =====================================================================
-- Runs every schema file in dependency order. Usage:
--   mysql -u <user> -p < database/schema.sql
-- (Run from the database/ directory, or adjust the SOURCE paths below
--  to be relative to wherever you invoke mysql from.)
-- =====================================================================

-- ==========================================================
-- Hospital Management System
-- Master Database Schema
-- Author: Team HMS
-- Database: hospital_management
-- MySQL Version: 8.0+
-- ==========================================================

-- Drop existing database (Development Only)
DROP DATABASE IF EXISTS hospital_management;

-- Create database
CREATE DATABASE hospital_management
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

-- Select database
USE hospital_management;

-- ==========================================================
-- Authentication Module
-- ==========================================================

SOURCE schema/01_roles.sql;
SOURCE schema/02_users.sql;

-- ==========================================================
-- Hospital Module
-- ==========================================================

SOURCE schema/03_departments.sql;
SOURCE schema/04_doctors.sql;
SOURCE schema/05_doctor_availability.sql;

-- ==========================================================
-- Patient Module
-- ==========================================================

SOURCE schema/06_patients.sql;
SOURCE schema/07_emergency_contacts.sql;
SOURCE schema/08_patient_reports.sql;

-- ==========================================================
-- Appointment Module
-- ==========================================================

SOURCE schema/09_appointments.sql;

-- ==========================================================
-- Medical Module
-- ==========================================================

SOURCE schema/10_medical_records.sql;
SOURCE schema/11_prescriptions.sql;
SOURCE schema/12_prescription_items.sql;

-- ==========================================================
-- Pharmacy Module
-- ==========================================================

SOURCE schema/13_suppliers.sql;
SOURCE schema/14_medicines.sql;
SOURCE schema/15_inventory.sql;

-- ==========================================================
-- Billing Module
-- ==========================================================

SOURCE schema/16_bills.sql;
SOURCE schema/17_payments.sql;

-- ==========================================================
-- System Module
-- ==========================================================

SOURCE schema/18_notifications.sql;
SOURCE schema/19_audit_logs.sql;

-- ==========================================================
-- Database Schema Created Successfully
-- ==========================================================
