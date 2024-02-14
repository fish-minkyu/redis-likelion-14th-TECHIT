INSERT INTO item (name,description,price,stock)
VALUES
  ('mouse','purus,',68707,22),
  ('monitor','ullamcorper',11034,80),
  ('keyboard','magnis dis parturient',37700,43),
  ('speaker','faucibus leo, in lobortis',58281,82),
  ('mouse','at pretium',61395,85),
  ('monitor','massa rutrum',53854,54),
  ('keyboard','vulputate dui, nec',10952,58),
  ('speaker','gravida sagittis.',18103,57),
  ('mouse','tellus. Nunc lectus',81846,36),
  ('monitor','sagittis augue,',23507,59);
INSERT INTO item (name,description,price,stock)
VALUES
  ('keyboard','ultricies ornare, elit elit',25511,94),
  ('speaker','est ac',19597,57),
  ('mouse','ut nisi',13688,61),
  ('monitor','natoque penatibus et',62116,38),
  ('keyboard','vulputate mauris',25028,41),
  ('speaker','Quisque ac libero nec',22685,48),
  ('mouse','lobortis ultrices. Vivamus',32101,54),
  ('monitor','arcu. Morbi sit',56267,37),
  ('keyboard','Mauris vel',48496,58),
  ('speaker','in',70633,71);
INSERT INTO item (name,description,price,stock)
VALUES
  ('mouse','neque. Nullam',32901,19),
  ('monitor','eu tellus. Phasellus',35059,44),
  ('keyboard','posuere cubilia Curae Phasellus',22529,71),
  ('speaker','Nunc commodo auctor',94930,68),
  ('mouse','risus. Donec egestas. Aliquam',16251,33),
  ('monitor','et malesuada fames',60813,67),
  ('keyboard','justo.',33390,69),
  ('speaker','sem mollis',56080,97),
  ('mouse','sit',19070,19),
  ('monitor','In at pede.',33048,71);
INSERT INTO item (name,description,price,stock)
VALUES
  ('keyboard','vulputate, posuere vulputate, lacus.',45197,30),
  ('speaker','nulla ante,',96083,98),
  ('mouse','mauris. Morbi',70104,13),
  ('monitor','malesuada id, erat. Etiam',55592,46),
  ('keyboard','facilisis facilisis,',89730,32),
  ('speaker','posuere',71030,94),
  ('mouse','nec',26254,56),
  ('monitor','neque. Nullam ut',61081,58),
  ('keyboard','scelerisque neque.',77121,13),
  ('speaker','congue, elit sed',47105,24);

UPDATE item
SET price = (price / 1000) * 1000;