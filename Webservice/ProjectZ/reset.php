<?php

function my_rand()
{
	$r = rand();
	while ($r >= 10000 || $r <= 1000) {
		$r = rand();
		# code...
	}
	return $r;
}

if ($_SERVER['REQUEST_METHOD'] == 'GET') 
{
    if ($key = $_GET['key']) 
    {

        $conn = mysqli_connect('localhost', 'root', '', 'projectz');
        //var_dump($conn);
        $sql = "DELETE FROM ballz WHERE true;     ";

        $sql2 = "INSERT INTO `ballz` (`id`, `longitude`, `latitude`) VALUES
(1, '39.949". my_rand()%1000 ."', '116.34". my_rand() ."'),
(2, '39.949". my_rand()%1000 ."', '116.34". my_rand() ."'),
(3, '39.949". my_rand()%1000 ."', '116.34". my_rand() ."'),
(4, '39.949". my_rand()%1000 ."', '116.34". my_rand() ."'),
(5, '39.94981". my_rand()%10 ."', '116.34". my_rand() ."'),
(6, '39.94985". my_rand()%10 ."', '116.34". my_rand() ."'),
(7, '39.949801', '116.343820');";

        if (mysqli_connect_errno()) 
        {
            echo json_encode(array(
                'status' => 'failure',
                'message' => 'Could Not connect to database',
            ));
        }

        $data = mysqli_query($conn, $sql);
        $data2 = mysqli_query($conn, $sql2);

        //echo json_encode( mysqli_fetch_all($data) );

        if ($data && $key == "*ProjectZ*") 
        {
            echo '{"0":"0","requestType":"RESET"}';
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
