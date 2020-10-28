<?php 
$DB_NAME = "meal_plan2";
$DB_USER = "root";
$DB_PASS = "";
$DB_SERVER_LOC = "localhost";

if($_SERVER['REQUEST_METHOD'] == 'POST'){
	$conn = mysqli_connect($DB_SERVER_LOC, $DB_USER, $DB_PASS, $DB_NAME);
	$respon = array(); $respon['kode'] = '000';
	$nome = $_POST['user_name'];
	$cpf = $_POST['user_cpf'];
	$senha = $_POST['user_pass'];
	$role_id = $_POST['role_id'];


	$sql = "INSERT INTO users(user_name, user_cpf, user_password, role_id) VALUES(
	'$nome', '$cpf', '$senha', '$role_id') ";
	$result = mysqli_query($conn, $sql);
	if ($result) {
		echo json_encode($respon); exit();
	} else {
		$respon['kode'] = '111';
		echo json_encode($respon); exit();
	}


}

?>