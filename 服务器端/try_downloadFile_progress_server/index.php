<?php
header ( 'Content-type:text/json' ); // ������ص㣬�����߽������ݵĶ����ҳ���������json���ݣ�
/**
 *��������ķ���ֵ����������ģ����ȷ��ص�JSON��������
 *����������ȷ��id=1;���г���id=0;
 *value�ֶΣ���Ӧ״̬��
 */
$action = $_POST ['action'];
switch($action){
	case 'checkNewestVersion'://������°汾
		checkNewestVersion();
		break;
	default :
		break;
}

/**
 * ��ѯ��ǰ���°汾��
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