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
	$foods_id = $_POST['food_id'];

	$category_name = $_POST['category_name'];
	$sql = "SELECT categories.category_id FROM categories WHERE categories.category_name = '$category_name'";

	$result = mysqli_query($conn,$sql);

	if (mysqli_num_rows($result)>0){


		$data_mhs1 = array();
		while($mhs1 = mysqli_fetch_assoc($result)){
			array_push($data_mhs1, $mhs1);				
		}
		
		$sel_id = $data_mhs1[0]['category_id'];

		

			$sql2 = "UPDATE foods SET food_name = '$nome', food_description = '$desc', food_price = '$valor', category_id = '$sel_id' WHERE foods.food_id = $foods_id";
			$result2 = mysqli_query($conn, $sql2);
				if ($result2) {
					echo json_encode($respon); exit();
				} else {
					$respon['kode'] = '111';
					echo json_encode($respon); exit();
					}

	}

	

	


}

?>