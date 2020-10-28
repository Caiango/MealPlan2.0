<?php 
$DB_NAME = "meal_plan2";
$DB_USER = "root";
$DB_PASS = "";
$DB_SERVER_LOC = "localhost";


if($_SERVER['REQUEST_METHOD'] == 'POST'){
	$conn = mysqli_connect($DB_SERVER_LOC, $DB_USER, $DB_PASS, $DB_NAME);
	$category_name = $_POST['category_name'];
	$sql = "SELECT categories.category_id FROM categories WHERE categories.category_name = '$category_name'";

	$result = mysqli_query($conn,$sql);

	if (mysqli_num_rows($result)>0) {
		header("Access-Control-Allow-Origin: *");
		header("Content-type: application/json; charset=UTF-8");

		$data_mhs1 = array();
		while($mhs1 = mysqli_fetch_assoc($result)){
			array_push($data_mhs1, $mhs1);				
		}
		

		$sel_id = $data_mhs1[0]['category_id'];
		if ($category_name == "Todos") {
			$sql2 = "SELECT * FROM foods";
		} else {
			$sql2 = "SELECT * FROM foods WHERE foods.category_id = '$sel_id'";
		}

		

		$result2 = mysqli_query($conn,$sql2);


		$data_mhs = array();
		while($mhs = mysqli_fetch_assoc($result2)){
			array_push($data_mhs, $mhs);				
		}
		echo json_encode($data_mhs);
		
	}
	

}

?>	