


    <?php
    //Start session
    session_start();
     
    //Include database connection details
    require_once('connection.php');
     
    //Array to store validation errors
    $errmsg_arr = array();
     
    //Validation error flag
    $errflag = false;
     
    $password=$_POST["password"]; 
	$username=$_POST["username"]; 
	if (!empty($_POST)) 
	{ 
	if (empty($_POST['username']) || empty($_POST['password'])) 
	{ // Create some data that will be the JSON response 
$response["success"] = 0; 
$response["message"] = "One or both of the fields are empty .";
 //die is used to kill the page, will not let the code below to be executed. It will also 
 //display the parameter, that is the json data which our android application will parse to be
 //shown to the users 
 die(json_encode($response));
 }
 
  $qry="SELECT * FROM auth WHERE uid='$username' AND pass='$password'";
    $result=mysql_query($qry);
 
 $row = mysql_fetch_array($result); 
 
 if (!empty($row)) { $response["success"] = 1; 
 $response["message"] = "You have been sucessfully login";
 die(json_encode($response)); } 
 else{ $response["success"] = 0;
 $response["message"] = "invalid username or password "; 
 die(json_encode($response)); } } else{ $response["success"] = 0; 
 $response["message"] = " One or both of the fields are empty "; 
 die(json_encode($response)); } mysql_close(); 
 ?>

