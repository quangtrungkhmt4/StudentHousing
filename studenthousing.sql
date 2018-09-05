-- phpMyAdmin SQL Dump
-- version 4.8.2
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Aug 25, 2018 at 10:17 AM
-- Server version: 10.1.34-MariaDB
-- PHP Version: 7.2.7

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `studenthousing`
--

-- --------------------------------------------------------

--
-- Table structure for table `tblbooks`
--

CREATE TABLE `tblbooks` (
  `IDBOOK` int(11) NOT NULL,
  `IDUSER` int(11) NOT NULL,
  `IDHOUSE` int(11) NOT NULL,
  `CHECK_SEEN` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `tblcomments`
--

CREATE TABLE `tblcomments` (
  `IDCOMMENT` int(11) NOT NULL,
  `TEXT` text NOT NULL,
  `IDUSER` int(11) NOT NULL,
  `IDHOUSE` int(11) NOT NULL,
  `CREATED_AT` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `tblfavorites`
--

CREATE TABLE `tblfavorites` (
  `IDFAV` int(11) NOT NULL,
  `IDUSER` int(11) NOT NULL,
  `IDHOUSE` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `tblhousers`
--

CREATE TABLE `tblhousers` (
  `IDHOUSE` int(11) NOT NULL,
  `TITLE` text NOT NULL,
  `ADDRESS` text NOT NULL,
  `OBJECT` int(11) NOT NULL,
  `IMAGE` text NOT NULL,
  `DESC` text NOT NULL,
  `CONTACT` text NOT NULL,
  `ACREAGE` float NOT NULL,
  `PRICE` float NOT NULL,
  `MAXPEO` int(11) NOT NULL,
  `CREATED_AT` text NOT NULL,
  `CHECK_UP` int(11) NOT NULL,
  `STATE` int(11) NOT NULL,
  `IDUNIT` int(11) NOT NULL,
  `IDUSER` int(11) NOT NULL,
  `LATLNG` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `tblinfoimages`
--

CREATE TABLE `tblinfoimages` (
  `IDIMAGE` int(11) NOT NULL,
  `URL` text NOT NULL,
  `IDHOUSE` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `tblregisterposter`
--

CREATE TABLE `tblregisterposter` (
  `IDREGISTER` int(11) NOT NULL,
  `IDUSER` int(11) NOT NULL,
  `CONFIRM` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `tblunits`
--

CREATE TABLE `tblunits` (
  `IDUNIT` int(11) NOT NULL,
  `TYPE` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tblunits`
--

INSERT INTO `tblunits` (`IDUNIT`, `TYPE`) VALUES
(1, 'triệu/tháng');

-- --------------------------------------------------------

--
-- Table structure for table `tblusers`
--

CREATE TABLE `tblusers` (
  `IDUSER` int(11) NOT NULL,
  `USER` varchar(30) NOT NULL,
  `PASSWORD` varchar(30) NOT NULL,
  `NAME` varchar(50) NOT NULL,
  `PHONE` varchar(12) NOT NULL,
  `PERMISSION` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `tblbooks`
--
ALTER TABLE `tblbooks`
  ADD PRIMARY KEY (`IDBOOK`),
  ADD KEY `tblBooks_fk0` (`IDUSER`),
  ADD KEY `tblBooks_fk1` (`IDHOUSE`);

--
-- Indexes for table `tblcomments`
--
ALTER TABLE `tblcomments`
  ADD PRIMARY KEY (`IDCOMMENT`),
  ADD KEY `tblComments_fk0` (`IDUSER`),
  ADD KEY `tblComments_fk1` (`IDHOUSE`);

--
-- Indexes for table `tblfavorites`
--
ALTER TABLE `tblfavorites`
  ADD PRIMARY KEY (`IDFAV`),
  ADD KEY `tblFavorites_fk0` (`IDUSER`),
  ADD KEY `tblFavorites_fk1` (`IDHOUSE`);

--
-- Indexes for table `tblhousers`
--
ALTER TABLE `tblhousers`
  ADD PRIMARY KEY (`IDHOUSE`),
  ADD KEY `tblHousers_fk0` (`IDUNIT`),
  ADD KEY `tblHousers_fk1` (`IDUSER`);

--
-- Indexes for table `tblinfoimages`
--
ALTER TABLE `tblinfoimages`
  ADD PRIMARY KEY (`IDIMAGE`),
  ADD KEY `tblInfoImages_fk0` (`IDHOUSE`);

--
-- Indexes for table `tblregisterposter`
--
ALTER TABLE `tblregisterposter`
  ADD PRIMARY KEY (`IDREGISTER`),
  ADD KEY `tblRegisterPoster_fk0` (`IDUSER`);

--
-- Indexes for table `tblunits`
--
ALTER TABLE `tblunits`
  ADD PRIMARY KEY (`IDUNIT`);

--
-- Indexes for table `tblusers`
--
ALTER TABLE `tblusers`
  ADD PRIMARY KEY (`IDUSER`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `tblbooks`
--
ALTER TABLE `tblbooks`
  MODIFY `IDBOOK` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=26;

--
-- AUTO_INCREMENT for table `tblcomments`
--
ALTER TABLE `tblcomments`
  MODIFY `IDCOMMENT` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `tblfavorites`
--
ALTER TABLE `tblfavorites`
  MODIFY `IDFAV` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `tblhousers`
--
ALTER TABLE `tblhousers`
  MODIFY `IDHOUSE` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=60;

--
-- AUTO_INCREMENT for table `tblinfoimages`
--
ALTER TABLE `tblinfoimages`
  MODIFY `IDIMAGE` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=39;

--
-- AUTO_INCREMENT for table `tblregisterposter`
--
ALTER TABLE `tblregisterposter`
  MODIFY `IDREGISTER` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `tblunits`
--
ALTER TABLE `tblunits`
  MODIFY `IDUNIT` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `tblusers`
--
ALTER TABLE `tblusers`
  MODIFY `IDUSER` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `tblbooks`
--
ALTER TABLE `tblbooks`
  ADD CONSTRAINT `tblBooks_fk0` FOREIGN KEY (`IDUSER`) REFERENCES `tblusers` (`IDUSER`),
  ADD CONSTRAINT `tblBooks_fk1` FOREIGN KEY (`IDHOUSE`) REFERENCES `tblhousers` (`IDHOUSE`);

--
-- Constraints for table `tblcomments`
--
ALTER TABLE `tblcomments`
  ADD CONSTRAINT `tblComments_fk0` FOREIGN KEY (`IDUSER`) REFERENCES `tblusers` (`IDUSER`),
  ADD CONSTRAINT `tblComments_fk1` FOREIGN KEY (`IDHOUSE`) REFERENCES `tblhousers` (`IDHOUSE`);

--
-- Constraints for table `tblfavorites`
--
ALTER TABLE `tblfavorites`
  ADD CONSTRAINT `tblFavorites_fk0` FOREIGN KEY (`IDUSER`) REFERENCES `tblusers` (`IDUSER`),
  ADD CONSTRAINT `tblFavorites_fk1` FOREIGN KEY (`IDHOUSE`) REFERENCES `tblhousers` (`IDHOUSE`);

--
-- Constraints for table `tblhousers`
--
ALTER TABLE `tblhousers`
  ADD CONSTRAINT `tblHousers_fk0` FOREIGN KEY (`IDUNIT`) REFERENCES `tblunits` (`IDUNIT`),
  ADD CONSTRAINT `tblHousers_fk1` FOREIGN KEY (`IDUSER`) REFERENCES `tblusers` (`IDUSER`);

--
-- Constraints for table `tblinfoimages`
--
ALTER TABLE `tblinfoimages`
  ADD CONSTRAINT `tblInfoImages_fk0` FOREIGN KEY (`IDHOUSE`) REFERENCES `tblhousers` (`IDHOUSE`);

--
-- Constraints for table `tblregisterposter`
--
ALTER TABLE `tblregisterposter`
  ADD CONSTRAINT `tblRegisterPoster_fk0` FOREIGN KEY (`IDUSER`) REFERENCES `tblusers` (`IDUSER`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
