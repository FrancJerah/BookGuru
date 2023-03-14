-- phpMyAdmin SQL Dump
-- version 5.0.4
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Mar 31, 2022 at 08:07 AM
-- Server version: 10.4.17-MariaDB
-- PHP Version: 8.0.0

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `ancuin_db`
--

-- --------------------------------------------------------

--
-- Table structure for table `custtbl`
--

CREATE TABLE `custtbl` (
  `ID` int(100) NOT NULL,
  `Fullname` varchar(500) NOT NULL,
  `Gender` varchar(500) NOT NULL,
  `CivilStatus` varchar(500) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `custtbl`
--

INSERT INTO `custtbl` (`ID`, `Fullname`, `Gender`, `CivilStatus`) VALUES
(1, 'Isazaki Tamaki', 'Male', 'Single'),
(2, 'Haruhaki Shizumi', 'Female', 'Single');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `custtbl`
--
ALTER TABLE `custtbl`
  ADD PRIMARY KEY (`ID`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `custtbl`
--
ALTER TABLE `custtbl`
  MODIFY `ID` int(100) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
