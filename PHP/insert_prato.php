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
	$cat_id = $_POST['category_id'];


	$sql = "INSERT INTO foods(food_name, food_description, food_price, category_id) VALUES(
	'$nome', '$desc', '$valor', '$cat_id') ";
	$result = mysqli_query($conn, $sql);
	if ($result) {
		echo json_encode($respon); exit();
	} else {
		$respon['kode'] = '111';
		echo json_encode($respon); exit();
	}


}

?>