(ns webcrawler.output)

(defn calculate-indent
  	[inv-depth]
  	(str (apply str (take inv-depth (repeat "-"))), "> "))

(defn- get-status-representation
	[p-node]
	(case (:status p-node)
		200 ""
		404 " bad"
		(301 302) " redirected"))

(defn- generate-node-info
	[p-node]
	(let [urls-count (count (:urls p-node))
		  status-representation (get-status-representation p-node)]
		(str (:url p-node), " ", urls-count, status-representation)))

(defn- print-node
  	[p-node, inv-depth]
  	(let [str-indent (calculate-indent (* 3 inv-depth))]
   		(println str-indent (generate-node-info p-node))))

(defn print-nodes
	[p-node, depth]
  (if (> depth 0)
	  (print-node p-node depth))
	(doseq [child-node (:children p-node)]
		(print-nodes child-node (inc depth))))
