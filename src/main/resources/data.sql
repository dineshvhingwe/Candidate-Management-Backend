insert into addresses (addressline, city, pin, town, address_id) values ('addressline11', 'somecity', 411111, 'someTown', 'f6f490ee-ea89-4046-abc7-ecbfb2739bec');

insert into candidates (address_address_id, date_created, email, gender, image_url, last_updated, name, phone, surname, candidate_id) values ('f6f490ee-ea89-4046-abc7-ecbfb2739bec', '2022-07-12 14:13:55', 'someone@someemail.com', 0, 'www.myimage.com/myimage', '2022-07-12 14:13:55', 'somename', 8606060660, 'mysurname', '707f1cee-7898-45f5-93f2-a0eb8e376b0f');

insert into addresses (addressline, city, pin, town, address_id) values ('tcs address', 'tcscity', 123321, 'tcstown', '37c0a311-17ea-49d7-ba02-71a6f06f22a5');
insert into professions (candidate_id, designation, employer, period_from, period_to, address_id, profession_id) values ('707f1cee-7898-45f5-93f2-a0eb8e376b0f', 'Java Developer', 'TCS', '2016-10-10', '2019-10-10', '37c0a311-17ea-49d7-ba02-71a6f06f22a5', 'd760b829-3cd8-4d4e-b3d9-33e6ae5233b1');

insert into tags (id, name) values (1, 'Java');
insert into candidates_tags (candidate_id, tag_id) values ('707f1cee-7898-45f5-93f2-a0eb8e376b0f', 1);

insert into tags (id, name) values (2, 'SQL');
insert into candidates_tags (candidate_id, tag_id) values ('707f1cee-7898-45f5-93f2-a0eb8e376b0f', 2);
