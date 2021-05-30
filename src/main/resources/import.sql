/*populate tabla region*/
insert into region(id, nombre) values(1, 'Sudamerica');
insert into region(id, nombre) values(2, 'Centroamerica');
insert into region(id, nombre) values(3, 'Norteamaerica');
insert into region(id, nombre) values(4, 'Europa');
insert into region(id, nombre) values(5, 'Africa');
insert into region(id, nombre) values(6, 'Asia');

/*populate tabla cliente*/
insert into cliente(nombre, dni, fecha_nacimiento, saldo, puntos, region_id) values ('Junior', '71984315', '1994-06-14', 3000, 500, 3);
insert into cliente(nombre, dni, fecha_nacimiento, saldo, puntos, region_id) values ('Nombre1', '00000000', '2010-02-18', 100, 0, 4);
insert into cliente(nombre, dni, fecha_nacimiento, saldo, puntos, region_id) values ('Nombre2', '00000000', '2010-02-19', 100, 0, 1);
insert into cliente(nombre, dni, fecha_nacimiento, saldo, puntos, region_id) values ('Nombre3', '00000000', '2010-02-20', 100, 0, 2);
insert into cliente(nombre, dni, fecha_nacimiento, saldo, puntos, region_id) values ('Nombre4', '00000000', '2010-02-21', 100, 0, 2);
insert into cliente(nombre, dni, fecha_nacimiento, saldo, puntos, region_id) values ('Nombre5', '00000000', '2010-02-21', 2500, 400, 3);

/*populate usuarios*/
insert into usuarios(username, password, enabled, nombre, apellido, email) values ('junior', '$2a$10$AECwRguDx4.xlCsgwqLGAeOA/lT3Gdpjz2oYe2zYR/1v15WiEBBIu', 1, 'Junior', 'Oblitas', 'cjr.oblitas@gmail.com');
insert into usuarios(username, password, enabled, nombre, apellido, email) values ('mayte', '$2a$10$FQOiz7zkjpiKBDu.d2SIFu8phJppLt2RHRVadr2HJYnDsyou9Tnr.', 1, 'Mayte', 'Oblitas', 'mayte.oblitas@gmail.com');
insert into usuarios(username, password, enabled, nombre, apellido, email) values ('brad', '$2a$10$t8DNYuuV3rBTxJFPH9hkDuI8YiuIdeXlclgdnuGNeeL6YgJ6suAp2', 1, 'Brad', 'Oblitas', 'brad.oblitas@gmail.com');

/*populate roles*/
insert into roles(nombre) values ('ROLE_USER');
insert into roles(nombre) values ('ROLE_ADMIN');

/*populate usuarios_roles*/
insert into usuarios_roles(usuario_id, rol_id) values (1,1);
insert into usuarios_roles(usuario_id, rol_id) values (2,2);
insert into usuarios_roles(usuario_id, rol_id) values (2,1);