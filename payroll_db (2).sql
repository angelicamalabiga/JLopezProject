-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Oct 27, 2025 at 03:10 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `payroll_db`
--

-- --------------------------------------------------------

--
-- Table structure for table `attendance`
--

CREATE TABLE `attendance` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `period_start` date NOT NULL,
  `period_end` date NOT NULL,
  `file_name` varchar(255) NOT NULL,
  `file_path` varchar(255) NOT NULL,
  `hours_worked` decimal(10,0) NOT NULL,
  `late_minutes` int(11) NOT NULL,
  `absences` int(11) NOT NULL,
  `submitted_date` date NOT NULL,
  `verified_by` varchar(255) NOT NULL,
  `verified_date` date NOT NULL,
  `attendance_status` enum('Pending','Present','Absent','Late') NOT NULL DEFAULT 'Pending'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `attendance`
--

INSERT INTO `attendance` (`id`, `user_id`, `period_start`, `period_end`, `file_name`, `file_path`, `hours_worked`, `late_minutes`, `absences`, `submitted_date`, `verified_by`, `verified_date`, `attendance_status`) VALUES
(12468, 123, '0000-00-00', '0000-00-00', 'DTR_Report.pdf', 'DTR', 0, 0, 0, '0000-00-00', '', '0000-00-00', 'Pending');

-- --------------------------------------------------------

--
-- Table structure for table `cash_advances`
--

CREATE TABLE `cash_advances` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `amount` decimal(10,0) NOT NULL,
  `purpose` varchar(255) NOT NULL,
  `status` enum('Pending','Approved','Paid') NOT NULL DEFAULT 'Pending',
  `request_date` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `holidays`
--

CREATE TABLE `holidays` (
  `id` int(11) NOT NULL,
  `holiday_name` varchar(255) NOT NULL,
  `holiday_date` date NOT NULL,
  `type` enum('Regular Holiday','Special Holiday') NOT NULL,
  `description` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `leave_request`
--

CREATE TABLE `leave_request` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `start_date` date NOT NULL,
  `end_date` date NOT NULL,
  `status` enum('Pending','Approved','Denied') NOT NULL DEFAULT 'Pending',
  `date_request` date NOT NULL,
  `reason` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `payheads`
--

CREATE TABLE `payheads` (
  `id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `type` enum('Earnings','Deductions') NOT NULL,
  `amount` decimal(10,0) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `payroll`
--

CREATE TABLE `payroll` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `period_start` date NOT NULL,
  `period_end` date NOT NULL,
  `basic_pay` decimal(10,0) NOT NULL,
  `total_earnings` decimal(10,0) NOT NULL,
  `total_deductions` decimal(10,0) NOT NULL,
  `net_pay` decimal(10,0) NOT NULL,
  `date_generated` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` bigint(11) NOT NULL,
  `email` varchar(255) NOT NULL,
  `username` varchar(255) NOT NULL,
  `first_name` varchar(255) NOT NULL,
  `last_name` varchar(255) NOT NULL,
  `middle_name` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role` varchar(255) NOT NULL,
  `birth_date` date NOT NULL,
  `gender` enum('Male','Female') NOT NULL,
  `nationality` varchar(255) NOT NULL,
  `house_no` int(11) NOT NULL,
  `barangay` varchar(255) NOT NULL,
  `city` varchar(255) NOT NULL,
  `province` varchar(255) NOT NULL,
  `zip_code` int(11) NOT NULL,
  `contact_no` varchar(15) DEFAULT NULL,
  `emergency_no1` varchar(15) DEFAULT NULL,
  `position` varchar(255) NOT NULL,
  `joining_date` date NOT NULL,
  `status` enum('Active','Deactived') NOT NULL,
  `basic_salary` decimal(10,0) NOT NULL,
  `photo` varchar(255) NOT NULL,
  `created_at` date NOT NULL,
  `updated_at` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `email`, `username`, `first_name`, `last_name`, `middle_name`, `password`, `role`, `birth_date`, `gender`, `nationality`, `house_no`, `barangay`, `city`, `province`, `zip_code`, `contact_no`, `emergency_no1`, `position`, `joining_date`, `status`, `basic_salary`, `photo`, `created_at`, `updated_at`) VALUES
(12345678, 'lykamalabiga03@gmail.com', 'LykaRamos', 'Lyka', 'Malabiga', 'Ramos', '123', 'admin', '0000-00-00', 'Female', 'Filipino', 26, 'Santo Domingo', 'Cainta ', 'Rizal', 1900, '761332980', '123456789', 'Accountant', '0000-00-00', 'Active', 1000, 'a', '2025-10-25', '2025-10-25 02:33:19'),
(12345682, 'romnickmalabiga@gmail.com', 'romnick', 'Romnick', 'Malabiga', 'Ramos', '123', 'user', '2005-12-12', 'Male', 'filipino', 12, 'Sto nino', 'Marikina', 'Antipolo', 1900, '09761332980', '09761332980', 'System Analyst', '2025-10-25', 'Active', 500, 'C:\\Users\\angel\\OneDrive\\Pictures\\Screenshots\\Screenshot 2025-10-06 071558.png', '2025-10-25', '2025-10-25 10:51:42'),
(12345683, 'raindalecleivem@gmail.com', 'Rc', 'Raindale Cleive', 'Marquez', 'Sanchez', '123', 'user', '2005-06-15', 'Male', 'Filipinong Hilaw', 42, 'P Burgos Sto Nino', 'Marikina', 'Rizal', 1900, '09761332980', '09761332980', 'idk', '2025-10-27', 'Active', 700, 'C:\\Users\\angel\\OneDrive\\Pictures\\Roblox\\RobloxScreenShot20250621_215509678.png', '2025-10-27', '2025-10-27 09:10:21'),
(10101401980, 'spmanandeg3962ant@student.fatima.edu.ph', 'ashley', 'Sherrylee', 'Manandeg', 'Ashley', '123', 'user', '2004-02-03', 'Female', 'Filipino', 1, 'Sot', 'Rizal', 'Rizal', 129, '09120810829', '09819717297', 'idk', '2025-10-27', 'Active', 1000, 'C:\\Users\\angel\\OneDrive\\Pictures\\Screenshots\\Screenshot 2025-10-06 071558.png', '2025-10-27', '2025-10-27 09:42:00');

-- --------------------------------------------------------

--
-- Table structure for table `verification_codes`
--

CREATE TABLE `verification_codes` (
  `email` varchar(100) NOT NULL,
  `code` varchar(10) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `verification_codes`
--

INSERT INTO `verification_codes` (`email`, `code`, `created_at`) VALUES
('try@gmail.com', '797146', '2025-10-25 23:19:49');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `attendance`
--
ALTER TABLE `attendance`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `cash_advances`
--
ALTER TABLE `cash_advances`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `holidays`
--
ALTER TABLE `holidays`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `leave_request`
--
ALTER TABLE `leave_request`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `payheads`
--
ALTER TABLE `payheads`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `payroll`
--
ALTER TABLE `payroll`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `verification_codes`
--
ALTER TABLE `verification_codes`
  ADD PRIMARY KEY (`email`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `attendance`
--
ALTER TABLE `attendance`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12469;

--
-- AUTO_INCREMENT for table `cash_advances`
--
ALTER TABLE `cash_advances`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `holidays`
--
ALTER TABLE `holidays`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `leave_request`
--
ALTER TABLE `leave_request`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `payheads`
--
ALTER TABLE `payheads`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `payroll`
--
ALTER TABLE `payroll`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` bigint(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10101401981;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
