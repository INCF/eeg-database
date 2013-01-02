alter table
  PERSON
  modify
  (
    NAME encrypt,
    SURNAME encrypt,
    DATE_OF_BIRTH encrypt,
    email encrypt no salt,
    fb_uid encrypt no salt,
    phone_number encrypt
  );
