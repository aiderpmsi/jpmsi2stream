-- index sur pmsiinsertion
CREATE INDEX pmsiinsertion_dateajout_idx
  ON pmsiinsertion USING btree (dateajout);