-- Necessary Database Creation
create database influentiadb_contentms;

create database influentiadb_subscriptionms;

create database influentiadb_accountms;

-- Using the necessary databases
use influentiadb_contentms;

use influentiadb_subscriptionms;

use influentiadb_accountms;

-- Describing the tables of contentMS DB
describe subscriptionplanlimits;

describe userposts;

-- Describing the tables of accountMS DB
describe socialaccounttracker;

describe socialaccounttypes;

describe usersocialaccounts;

-- Describing the tables of subscriptionMS DB
describe usersubscriptions;

describe subscriptionplans;

describe subscription_cancellations;