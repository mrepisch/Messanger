CREATE TABLE user(
				p_userID INTEGER PRIMARY KEY,
				userName VARCHAR(20) NOT NULL,
				password VARCHAR(50) NOT NULL
				);
				
				
CREATE TABLE chat(
				p_chatID INTEGER PRIMARY KEY,
				f_user1 VARCHAR(20) NOT NULL,
				f_user2 VARCHAR(20) NOT NULL,
				FOREIGN KEY(f_user1) REFERENCES user(p_userID),
				FOREIGN KEY(f_user2) REFERENCES user(p_userID)
				);
				
CREATE TABLE message(
				p_messageID INTEGER PRIMARY KEY,
				text VARCHAR,
				date DATETIME NOT NULL,
				f_sentBy VARCHAR(20) NOT NULL,
				f_chatID INTEGER NOT NULL,
				FOREIGN KEY(f_chatID) REFERENCES chat(p_chatID),
				FOREIGN KEY(sentBy) REFERENCES user(p_userID)
				);