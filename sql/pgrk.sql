SELECT hostname,page, count FROM domain
LEFT JOIN (SELECT cid,count(cid) FROM link GROUP BY cid) l
ON id = l.cid
ORDER BY count desc;