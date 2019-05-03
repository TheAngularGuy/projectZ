<?php

if ($_SERVER['REQUEST_METHOD'] == 'GET') {
	
    //parse_str(file_get_contents("php://input"), $_DELETE);

    if (($id = $_GET['id']) && ($key = $_GET['key'])) 
    {

        $conn = mysqli_connect('localhost', 'root', '', 'projectz');
        //var_dump($conn);
        $sql = "
                    DELETE FROM ballz WHERE id='$id'
               ";
			   

        if (mysqli_connect_errno()) {

            echo json_encode(array(
                'status' => 'failure',
                'message'=>'Could Not connect to database',
            ));
        }

        $data = mysqli_query($conn, $sql);

        if ($data && $key == "*ProjectZ*") {

            echo '{"id":"';
            echo $id;//$_GET['id'];
            echo '","requestType":"DEL"}';
            echo ";";
            echo json_encode(array(
                'status' => 'successful',
            ));

        } else {

            echo json_encode(array(
                'status' => 'failure',
            ));
        }
    }
}
?>
