<?php 
$DB_NAME = "meal_plan2";
$DB_USER = "root";
$DB_PASS = "";
$DB_SERVER_LOC = "localhost";

if($_SERVER['REQUEST_METHOD'] == 'POST'){
	$conn = mysqli_connect($DB_SERVER_LOC, $DB_USER, $DB_PASS, $DB_NAME);
	$respon = array(); $respon['kode'] = '000';
	$user_id = $_POST['user_id'];
	

	$sql = "DELETE FROM users WHERE users.user_id = $user_id";
	$result = mysqli_query($conn, $sql);
	if ($result) {
		echo json_encode($respon); exit();
	} else {
		$respon['kode'] = '111';
		echo json_encode($respon); exit();
	}


}

?>