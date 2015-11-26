<?php
header ( 'Content-type:text/json' ); // 这句是重点，它告诉接收数据的对象此页面输出的是json数据；
/**
 *整个程序的返回值是这样定义的：首先返回的JSON类型数据
 *程序运行正确，id=1;运行出错，id=0;
 *value字段，对应状态码
 */
$action = $_POST ['action'];
switch($action){
	case 'checkNewestVersion'://检查最新版本
		checkNewestVersion();
		break;
	default :
		break;
}

/**
 * 查询当前最新版本号
 */
function checkNewestVersion()
{
	$returnArray = array ();

	$a = array (
			'id' => "1",
			'verName' => "1.0.1",
			'verCode' => "2"
	);
	$returnArray [] = $a;

	$returnValue = json_encode ( $returnArray );
	echo $returnValue;
}