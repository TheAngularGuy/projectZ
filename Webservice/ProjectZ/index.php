<?php

if ($_SERVER['REQUEST_METHOD'] == 'GET') 
{
    if ($key = $_GET['key']) 
    {

        $conn = mysqli_connect('localhost', 'root', '', 'projectz');
        //var_dump($conn);
        $sql = "SELECT * FROM ballz WHERE true;     ";

        if (mysqli_connect_errno()) 
        {
            echo json_encode(array(
                'status' => 'failure',
                'message' => 'Could Not connect to database',
            ));
        }

        $data = mysqli_query($conn, $sql);

        //echo json_encode( mysqli_fetch_all($data) );

        if ($data && $key == "*ProjectZ*") 
        {
            echo '{"sql":"Ok","requestType":"TEST"}';
            echo ";";
        }
        else {

            echo json_encode(array(
                'status' => 'failure',
            ));
        }
    }
}
?>
