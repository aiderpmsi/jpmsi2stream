-- Pour la table pmsiinsertion
ALTER TABLE pmsiinsertion
  ADD CONSTRAINT pmsiinsertion_pmsiinsertionid_fk FOREIGN KEY (pmsiinsertionid)
  REFERENCES pmsiinsertionresult (pmsiinsertionid)
  DEFERRABLE INITIALLY DEFERRED;

-- Pour la table pmsiinsertionresult
ALTER TABLE pmsiinsertionresult
  ADD CONSTRAINT pmsiinsertionresult_pmsiinsertionid_fk FOREIGN KEY (pmsiinsertionid)
  REFERENCES pmsiinsertion (pmsiinsertionid)
  DEFERRABLE INITIALLY DEFERRED;