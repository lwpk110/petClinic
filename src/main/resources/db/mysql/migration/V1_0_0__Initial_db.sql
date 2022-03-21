create table owners
(
  id                 int auto_increment primary key,
  created_date       datetime     null,
  last_modified_date datetime     null,
  first_name         varchar(10)  not null,
  last_name          varchar(10)  null,
  address            varchar(255) null,
  city               varchar(15)  null,
  telephone          varchar(255) null
);

create table pet_types
(
  id                 int auto_increment primary key,
  created_date       datetime    null,
  last_modified_date datetime    null,
  name               varchar(20) null
);

create table pets
(
  id                 int auto_increment primary key,
  created_date       datetime    null,
  last_modified_date datetime    null,
  name               varchar(20) null,
  birth_date         date        null,
  type_id            int         null,
  owner_id           int         null,
  constraint FK29s2i29slv68r27i7mt5qnq6k
    foreign key (type_id) references pet_types (id),
  constraint FK6teg4kcjcnjhduguft56wcfoa
    foreign key (owner_id) references owners (id)
);

create table specialtys
(
  id                 int auto_increment primary key,
  created_date       datetime    null,
  last_modified_date datetime    null,
  name               varchar(20) null
);

create table vets
(
  id                 int auto_increment primary key,
  created_date       datetime    null,
  last_modified_date datetime    null,
  first_name         varchar(10) not null,
  last_name          varchar(10) null
);

create table vet_specialties
(
  vet_id       int not null,
  specialty_id int not null,
  constraint FKby1c0fbaa0byaifi63vt18sx9 foreign key (vet_id) references vets (id),
  constraint FKdjs9ke69k0vqu01k4qq7ow2xd foreign key (specialty_id) references specialtys (id)
);

create table visits
(
  id          bigint auto_increment primary key,
  visit_date  date         null,
  description varchar(255) null,
  pet_id      int          null,
  constraint FK6jcifhlqqlsfseu67utlouauy foreign key (pet_id) references pets (id)
);

