<?php
require("config.php");
$sql = "UPDATE custtbl SET Fullname = " . $_REQUEST["fname"] . ", Gender = ".$_REQUEST["gen"] . ",
 CivilStatus = ". $_REQUEST["civstats"]."WHERE ID = '". $_REQUEST["id"]. "'";

try {
    $dbrecords = mysqli_query($connect,$sql);
} catch (Exception $e) {
    $response["success"] = 0;
    $response["message"] = "Database Error #1. Please Try Again!";
    die(json_encode($response));
}
    $response["success"] = 0;
    $response["message"] = "Record Updated";
    die(json_encode($response))

?>