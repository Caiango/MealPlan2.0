<?php 
	$DB_NAME = "meal_plan2";
	$DB_USER = "root";
	$DB_PASS = "";
	$DB_SERVER_LOC = "localhost";

	if($_SERVER['REQUEST_METHOD'] == 'POST'){
		$conn = mysqli_connect($DB_SERVER_LOC, $DB_USER, $DB_PASS, $DB_NAME);
		$sql = "SELECT food_id, food_name, food_description, category_id, food_price, concat('http://192.168.1.7/meal_plan2/photos/', photos) as url FROM foods";
		$result = mysqli_query($conn,$sql);
		if (mysqli_num_rows($result)>0) {
			header("Access-Control-Allow-Origin: *");
			header("Content-type: application/json; charset=UTF-8");

			$data_mhs = array();
			while($mhs = mysqli_fetch_assoc($result)){
				array_push($data_mhs, $mhs);				
			}
			echo json_encode($data_mhs);
		}
	}

?>