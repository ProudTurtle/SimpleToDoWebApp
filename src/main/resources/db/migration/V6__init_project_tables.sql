create table projects
(
                             id int primary key auto_increment,
                             description varchar(100) not null
);
create table project_steps
(
                         id int primary key auto_increment,
                         description varchar(100) not null,
                         days_to_deadline int not null,
                         project_id int not null,
                         foreign key (project_id) references projects(id)
);
alter table TASKS_GROUPS add column project_id int null;
alter table TASKS_GROUPS add foreign key (project_id) references projects(id);