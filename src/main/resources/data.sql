INSERT INTO TB_USERS (LOGIN, PASSWORD) VALUES ('ADMIN', '$2a$12$Kb/YWYqtQpTPi/hc/Mqi/.Xq4nU4y2A/t4H1z/pTgQ5CDlHno7ORq');

INSERT INTO TB_PLAYLISTS (NOME, DESCRICAO) VALUES ('Rock', 'Lista de musicas MPB do Spotify');
INSERT INTO TB_SONGS (TITULO, ARTISTA, ALBUM, ANO, GENERO, PLAYLIST_ID) VALUES ('Master Of Puppets', 'Metallica', 'Master Of Puppets', '1986', 'Thrash Metal', 1);
INSERT INTO TB_SONGS (TITULO, ARTISTA, ALBUM, ANO, GENERO, PLAYLIST_ID) VALUES ('Aces High', 'Iron Maiden', 'Powerslave', '1984', 'Heavy Metal', 1);

INSERT INTO TB_PLAYLISTS (NOME, DESCRICAO) VALUES ('Musicas Eletronicas', 'Lista de musicas eletrônicas.');
INSERT INTO TB_SONGS (TITULO, ARTISTA, ALBUM, ANO, GENERO, PLAYLIST_ID) VALUES ('Favela', 'Ina Wroldsen, Alok', 'Favela', '2018', 'Eletrônica', 2);
INSERT INTO TB_SONGS (TITULO, ARTISTA, ALBUM, ANO, GENERO, PLAYLIST_ID) VALUES ('Save Me', 'Vintage Culture, Adam K ', 'Save Me', '2017', 'Alternative/Indie', 2);