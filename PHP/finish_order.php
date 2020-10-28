<?php 
$DB_NAME = "meal_plan2";
$DB_USER = "root";
$DB_PASS = "";
$DB_SERVER_LOC = "localhost";

if($_SERVER['REQUEST_METHOD'] == 'POST'){
	$conn = mysqli_connect($DB_SERVER_LOC, $DB_USER, $DB_PASS, $DB_NAME);
	$respon = array(); $respon['kode'] = '000';

	$order_id = $_POST['order_id'];
	

	$sql = "UPDATE orders SET done_order = 1 WHERE orders.order_id = $order_id";
	$result = mysqli_query($conn, $sql);
	if ($result) {
		echo json_encode($respon); exit();
	} else {
		$respon['kode'] = '111';
		echo json_encode($respon); exit();
	}


}

?>