-- 清理节点数据
DROP PROCEDURE IF EXISTS clearStorageData;
CREATE PROCEDURE clearStorageData()
BEGIN
  delete from md_list_storage_file_his;
	delete from md_list_storage_file;
	delete from md_tree_storage;
END;
call clearStorageData();