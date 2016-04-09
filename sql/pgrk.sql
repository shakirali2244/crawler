SELECT hostname,page, count(l) FROM domain
LEFT JOIN (SELECT cid,count(cid) FROM link GROUP BY cid) l
ON id = l.cid
ORDER BY count(l) desc;