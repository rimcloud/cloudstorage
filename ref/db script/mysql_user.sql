USE mysql;

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;

INSERT INTO user (Host, User, Select_priv, Insert_priv, Update_priv, Delete_priv, Create_priv, Drop_priv, Reload_priv, Shutdown_priv, Process_priv, File_priv, Grant_priv, References_priv, Index_priv, Alter_priv, Show_db_priv, Super_priv, Create_tmp_table_priv, Lock_tables_priv, Execute_priv, Repl_slave_priv, Repl_client_priv, Create_view_priv, Show_view_priv, Create_routine_priv, Alter_routine_priv, Create_user_priv, Event_priv, Trigger_priv, Create_tablespace_priv, ssl_type, ssl_cipher, x509_issuer, x509_subject, max_questions, max_updates, max_connections, max_user_connections, plugin, authentication_string, password_expired, password_last_changed, password_lifetime, account_locked) VALUES
('localhost', 'rim_admin', 'Y', 'Y', 'Y', 'Y', 'Y', 'Y', 'Y', 'Y', 'Y', 'Y', 'Y', 'Y', 'Y', 'Y', 'Y', 'Y', 'Y', 'Y', 'Y', 'Y', 'Y', 'Y', 'Y', 'Y', 'Y', 'Y', 'Y', 'Y', 'N', '', '0x', '0x', '0x', 0, 0, 0, 0, 'mysql_native_password', '*D9E2911AF48BFA966B85048D4B1D1C3388F93076', 'N', NULL, NULL, 'N'), 
('%', 'rim_admin', 'Y', 'Y', 'Y', 'Y', 'Y', 'Y', 'Y', 'Y', 'Y', 'Y', 'Y', 'Y', 'Y', 'Y', 'Y', 'Y', 'Y', 'Y', 'Y', 'Y', 'Y', 'Y', 'Y', 'Y', 'Y', 'Y', 'Y', 'Y', 'N', '', '0x', '0x', '0x', 0, 0, 0, 0, 'mysql_native_password', '*D9E2911AF48BFA966B85048D4B1D1C3388F93076', 'N', NULL, NULL, 'N');

/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

flush privileges;