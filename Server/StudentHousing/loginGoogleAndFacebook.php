<?php
	require "connect.php";

	$u = $_POST['user'];
	$p = $_POST['pass'];
	$n = $_POST['name'];
	$pho = $_POST['phone'];


	class User{
		function User($id, $user, $pass, $name, $phone, $permission){
			$this -> IDUSER = $id;
			$this -> USER = $user;
			$this -> PASSWORD = $pass;
			$this -> NAME = $name;
			$this -> PHONE = $phone;
			$this -> PERMISSION = $permission;
		}
	}

	if (strlen($u) > 0 && strlen($p) > 0 ) {
		$arrUser = array();
		$query = "SELECT * FROM tblUsers WHERE FIND_IN_SET('$u', USER)";
		$data = mysqli_query($connect, $query);
		if ($data) {
			while ($row = mysqli_fetch_assoc($data)) {
				array_push($arrUser, new User($row['IDUSER'], $row['USER'], $row['PASSWORD'], $row['NAME'], $row['PHONE'], $row['PERMISSION']));
			}

			if (count($arrUser) > 0) {
				echo json_encode($arrUser);
			}else{
				$insert = "INSERT INTO tblUsers VALUES(null, '$u', '$p', '$n', '$pho',0)";
				$dataInsert = mysqli_query($connect, $insert);
				if ($dataInsert) {


					$arr = array();
					$que = "SELECT * FROM tblUsers WHERE FIND_IN_SET('$u', USER) AND FIND_IN_SET('$p', PASSWORD)";
					$dt = mysqli_query($connect, $que);
					if ($dt) {
						while ($row = mysqli_fetch_assoc($dt)) {
							array_push($arr, new User($row['IDUSER'], $row['USER'], $row['PASSWORD'], $row['NAME'], $row['PHONE'], $row['PERMISSION']));
						}
						if (count($arr) > 0) {
							echo json_encode($arr);
						}else{
							echo "fail";
						}
					}
				}else{
					echo "fail";
				}
			}
		}
	}else{
		echo "null";
	}

?>
