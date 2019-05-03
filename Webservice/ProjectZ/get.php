<?php

if ($_SERVER['REQUEST_METHOD'] == 'GET') 
{
    if ($key = $_GET['key']) 
    {

        $conn = mysqli_connect('localhost', 'root', '', 'projectz');
        //var_dump($conn);
        $sql = "
                    SELECT * FROM ballz 
               ";

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
            echo '{"0":"0","requestType":"GET"}';
            echo ";";
            while ($row = mysqli_fetch_array($data)) 
            {
                echo json_encode($row);
                echo ";";
            }
        }
    }
}
?>
