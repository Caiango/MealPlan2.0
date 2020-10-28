<?php 
$DB_NAME = "meal_plan2";
$DB_USER = "root";
$DB_PASS = "";
$DB_SERVER_LOC = "localhost";

if($_SERVER['REQUEST_METHOD'] == 'POST'){
	$conn = mysqli_connect($DB_SERVER_LOC, $DB_USER, $DB_PASS, $DB_NAME);
	$respon = array(); $respon['kode'] = '000';
	$nome = $_POST['food_name'];
	$desc = $_POST['food_description'];
	$valor = $_POST['food_price'];
	$foods_id = $_POST['foods_id'];
	

	$sql = "UPDATE foods SET food_name = '$nome', food_description = '$desc', food_price = '$valor' WHERE foods.foods_id = $foods_id";
	$result = mysqli_query($conn, $sql);
	if ($result) {
		echo json_encode($respon); exit();
	} else {
		$respon['kode'] = '111';
		echo json_encode($respon); exit();
	}


}

?>