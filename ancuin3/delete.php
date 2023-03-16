<?php
require("config.php");
$sql = "DELETE FROM custtbl WHERE ID = '" . $_REQUEST["id"] . "'";
try {
    $dbrecords = mysqli_query($connect,$sql);
} catch (Exception $e) {
    $response["success"] = 0;
    $response["message"] = "Database Error#Delete. Please Try Again!";
    die(json_encode($response));
}
    $response["success"] = 0;
    $response["message"] = "Record Deleted";
    die(json_encode($response));
?>