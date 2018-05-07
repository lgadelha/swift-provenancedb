<?PHP
   header('Content-Type: text/plain');
   $cmd = $_GET['cmd'];
   system(escapeshellcmd("/home/provdb/provdb/bin/$cmd"));
#   system("/home/provdb/provdb/bin/$cmd");
?>
