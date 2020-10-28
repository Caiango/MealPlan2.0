<?php 
	$DB_NAME = "meal_plan2";
	$DB_USER = "root";
	$DB_PASS = "";
	$DB_SERVER_LOC = "localhost";

	if($_SERVER['REQUEST_METHOD'] == 'POST'){
	$conn = mysqli_connect($DB_SERVER_LOC, $DB_USER, $DB_PASS, $DB_NAME);
	$respon = array(); $respon['kode'] = '000';
	$user = array(); $user['user'] = '000';

	$rec_cpf = $_POST['rec_cpf'];
	$rec_pass = $_POST['rec_pass'];


	

	$sql = "SELECT * FROM users, roles WHERE users.user_cpf = '$rec_cpf' AND users.user_password = '$rec_pass' AND users.role_id = roles.role_id";
	$result = mysqli_query($conn, $sql);
	if ($result) {

		$data_mhs = array();
		while($mhs = mysqli_fetch_assoc($result)){
			array_push($data_mhs, $mhs);				
		}
		$current_user = $data_mhs[0]['user_name'];

		$user['user'] = $current_user;

		echo json_encode($data_mhs); exit();


	} else {
		$respon['kode'] = '111';
		echo json_encode($respon); exit();
	}


}

?>