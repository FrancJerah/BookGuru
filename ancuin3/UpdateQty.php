<?php
require("config.php");
$sql = "UPDATE booktbl SET BookTitle = " . $_REQUEST["book_title"] . ", Author = ".$_REQUEST["author_name"] . ",
Publisher = ".$_REQUEST["publisher_name"] . ", PublicationDate = ". $_REQUEST["publication_date"]
."WHERE ID = '". $_REQUEST["id"]. "'";

try {
    $dbrecords = mysqli_query($connect,$sql);
} catch (Exception $e) {
    $response["success"] = 0;
    $response["message"] = "Database Error #Update. Please Try Again!";
    die(json_encode($response));
}
    $response["success"] = 0;
    $response["message"] = "Record Updated";
    die(json_encode($response))

?>