(ns clojure-noob.chapter-6)
;; ## Clojure for the Brave and True

;; # Chapter 6

;; ## Notes:

;; ### Overview:

;; * What `def` does

;; * What namespaces are and how to use them

;; * The relationship between namespaces and the filesystem

;; * How to use `refer`, `alias`, `require`, `use`, and `ns`

;; * How to organize Clojure projects using the filesystem

;; ### Namespaces & Symbols

*ns*

(ns-name *ns*)

inc

'inc

(map inc [1 2])

'(map inc [1 2])





;; * Prefixing a list or symbol with `'` quotes the symbol preventing Clojure from evaluating it and uses it as data

;; * This is why when calling require directly the list of libraries must be quoted!

;; ### Storing Data

(def great-books ["East of Eden" "The Glass Bead Game"])

great-books



;; * The process of creating a var and having clojure create a reference to the address and the var name is called `interning` a var.

(ns-interns* *ns*)



;; > **NOTE:** *In a clojure context use `ns-interns` instead of `ns-interns*`*

(get (ns-interns* *ns*) 'great-books)

(keys (ns-interns* *ns*))

(ns-map *ns*)



;; * `ns-map` doesn't work in ClojureScript here but would output...

;; ```
;; '{primitives-classnames #'clojure.core/primitives-classnames, +' #'clojure.core/+', Enum java.lang.Enum, decimal? #'clojure.core/decimal?, restart-agent #'clojure.core/restart-agent, sort-by #'clojure.core/sort-by, macroexpand #'clojure.core/macroexpand, ensure #'clojure.core/ensure, chunk-first #'clojure.core/chunk-first, eduction #'clojure.core/eduction, tree-seq #'clojure.core/tree-seq, unchecked-remainder-int #'clojure.core/unchecked-remainder-int, seq #'clojure.core/seq, reduce #'clojure.core/reduce, InternalError java.lang.InternalError, when-first #'clojure.core/when-first, find-ns #'clojure.core/find-ns, get-thread-bindings #'clojure.core/get-thread-bindings, NullPointerException java.lang.NullPointerException, contains? #'clojure.core/contains?, every? #'clojure.core/every?, proxy-mappings #'clojure.core/proxy-mappings, keep-indexed #'clojure.core/keep-indexed, cond->> #'clojure.core/cond->>, subs #'clojure.core/subs, ref-min-history #'clojure.core/ref-min-history, set #'clojure.core/set, take-last #'clojure.core/take-last, InheritableThreadLocal java.lang.InheritableThreadLocal, bit-set #'clojure.core/bit-set, reader-conditional #'clojure.core/reader-conditional, gen-class #'clojure.core/gen-class, while #'clojure.core/while, ->Eduction #'clojure.core/->Eduction, butlast #'clojure.core/butlast, satisfies? #'clojure.core/satisfies?, Class java.lang.Class, line-seq #'clojure.core/line-seq, unchecked-subtract-int #'clojure.core/unchecked-subtract-int, take-nth #'clojure.core/take-nth, first #'clojure.core/first, re-groups #'clojure.core/re-groups, Error java.lang.Error, NoSuchFieldException java.lang.NoSuchFieldException, seq? #'clojure.core/seq?, dec' #'clojure.core/dec', ns-unmap #'clojure.core/ns-unmap, println-str #'clojure.core/println-str, with-bindings* #'clojure.core/with-bindings*, iterator-seq #'clojure.core/iterator-seq, Runtime java.lang.Runtime, iterate #'clojure.core/iterate, Cloneable java.lang.Cloneable, slurp #'clojure.core/slurp, StringIndexOutOfBoundsException java.lang.StringIndexOutOfBoundsException, newline #'clojure.core/newline, Object java.lang.Object, short-array #'clojure.core/short-array, fn? #'clojure.core/fn?, doall #'clojure.core/doall, prefers #'clojure.core/prefers, enumeration-seq #'clojure.core/enumeration-seq, dedupe #'clojure.core/dedupe, doc #'clojure.repl/doc, pprint #'clojure.pprint/pprint, dissoc #'clojure.core/dissoc, atom #'clojure.core/atom, import #'clojure.core/import, bit-shift-right #'clojure.core/bit-shift-right, print-method #'clojure.core/print-method, peek #'clojure.core/peek, VerifyError java.lang.VerifyError, aget #'clojure.core/aget, pvalues #'clojure.core/pvalues, bound-fn #'clojure.core/bound-fn, vswap! #'clojure.core/vswap!, last #'clojure.core/last, pr #'clojure.core/pr, LinkageError java.lang.LinkageError, namespace #'clojure.core/namespace, push-thread-bindings #'clojure.core/push-thread-bindings, bases #'clojure.core/bases, = #'clojure.core/=, dosync #'clojure.core/dosync, Process java.lang.Process, remove-ns #'clojure.core/remove-ns, take #'clojure.core/take, vector? #'clojure.core/vector?, ClassLoader java.lang.ClassLoader, thread-bound? #'clojure.core/thread-bound?, send-via #'clojure.core/send-via, boolean #'clojure.core/boolean, bit-shift-left #'clojure.core/bit-shift-left, find-var #'clojure.core/find-var, rand-int #'clojure.core/rand-int, aclone #'clojure.core/aclone, Double java.lang.Double, vreset! #'clojure.core/vreset!, chunk #'clojure.core/chunk, dec #'clojure.core/dec, future-call #'clojure.core/future-call, resultset-seq #'clojure.core/resultset-seq, struct #'clojure.core/struct, ThreadGroup java.lang.ThreadGroup, map #'clojure.core/map, juxt #'clojure.core/juxt, ns-publics #'clojure.core/ns-publics, < #'clojure.core/<, ThreadDeath java.lang.ThreadDeath, *source-path* #'clojure.core/*source-path*, with-loading-context #'clojure.core/with-loading-context, test #'clojure.core/test, rest #'clojure.core/rest, ex-data #'clojure.core/ex-data, compile #'clojure.core/compile, Callable java.util.concurrent.Callable, isa? #'clojure.core/isa?, .. #'clojure.core/.., munge #'clojure.core/munge, delay #'clojure.core/delay, set-error-mode! #'clojure.core/set-error-mode!, re-seq #'clojure.core/re-seq, char? #'clojure.core/char?, UnknownError java.lang.UnknownError, make-hierarchy #'clojure.core/make-hierarchy, set-agent-send-executor! #'clojure.core/set-agent-send-executor!, keep #'clojure.core/keep, char #'clojure.core/char, mapcat #'clojure.core/mapcat, unchecked-long #'clojure.core/unchecked-long, aset-long #'clojure.core/aset-long, some? #'clojure.core/some?, unchecked-negate #'clojure.core/unchecked-negate, gen-interface #'clojure.core/gen-interface, *command-line-args* #'clojure.core/*command-line-args*, reverse #'clojure.core/reverse, ClassCircularityError java.lang.ClassCircularityError, String java.lang.String, range #'clojure.core/range, sort #'clojure.core/sort, -cache-protocol-fn #'clojure.core/-cache-protocol-fn, unchecked-inc-int #'clojure.core/unchecked-inc-int, map-indexed #'clojure.core/map-indexed, with-bindings #'clojure.core/with-bindings, rand-nth #'clojure.core/rand-nth, comp #'clojure.core/comp, await #'clojure.core/await, spit #'clojure.core/spit, future-done? #'clojure.core/future-done?, *read-eval* #'clojure.core/*read-eval*, dorun #'clojure.core/dorun, disj #'clojure.core/disj, *2 #'clojure.core/*2, eval #'clojure.core/eval, cons #'clojure.core/cons, refer #'clojure.core/refer, print-dup #'clojure.core/print-dup, -reset-methods #'clojure.core/-reset-methods, floats #'clojure.core/floats, Void java.lang.Void, pos? #'clojure.core/pos?, fnil #'clojure.core/fnil, merge-with #'clojure.core/merge-with, nthrest #'clojure.core/nthrest, load #'clojure.core/load, if-not #'clojure.core/if-not, *verbose-defrecords* #'clojure.core/*verbose-defrecords*, sequential? #'clojure.core/sequential?, EnumConstantNotPresentException java.lang.EnumConstantNotPresentException, *print-level* #'clojure.core/*print-level*, shuffle #'clojure.core/shuffle, boolean-array #'clojure.core/boolean-array, find #'clojure.core/find, alength #'clojure.core/alength, bit-xor #'clojure.core/bit-xor, deliver #'clojure.core/deliver, doseq #'clojure.core/doseq, AbstractMethodError java.lang.AbstractMethodError, unsigned-bit-shift-right #'clojure.core/unsigned-bit-shift-right, neg? #'clojure.core/neg?, var-set #'clojure.core/var-set, unchecked-float #'clojure.core/unchecked-float, pmap #'clojure.core/pmap, error-mode #'clojure.core/error-mode, num #'clojure.core/num, reduced? #'clojure.core/reduced?, disj! #'clojure.core/disj!, StrictMath java.lang.StrictMath, float? #'clojure.core/float?, aset-float #'clojure.core/aset-float, deftype #'clojure.core/deftype, bean #'clojure.core/bean, booleans #'clojure.core/booleans, ns-unalias #'clojure.core/ns-unalias, when-let #'clojure.core/when-let, int-array #'clojure.core/int-array, set? #'clojure.core/set?, inc' #'clojure.core/inc', cat #'clojure.core/cat, *suppress-read* #'clojure.core/*suppress-read*, flush #'clojure.core/flush, take-while #'clojure.core/take-while, vary-meta #'clojure.core/vary-meta, <= #'clojure.core/<=, alter #'clojure.core/alter, -' #'clojure.core/-', if-some #'clojure.core/if-some, conj! #'clojure.core/conj!, repeatedly #'clojure.core/repeatedly, zipmap #'clojure.core/zipmap, alter-var-root #'clojure.core/alter-var-root, biginteger #'clojure.core/biginteger, remove #'clojure.core/remove, * #'clojure.core/*, re-pattern #'clojure.core/re-pattern, min #'clojure.core/min, pop! #'clojure.core/pop!, chunk-append #'clojure.core/chunk-append, prn-str #'clojure.core/prn-str, ArrayStoreException java.lang.ArrayStoreException, with-precision #'clojure.core/with-precision, format #'clojure.core/format, IllegalArgumentException java.lang.IllegalArgumentException, reversible? #'clojure.core/reversible?, shutdown-agents #'clojure.core/shutdown-agents, conj #'clojure.core/conj, bound? #'clojure.core/bound?, transduce #'clojure.core/transduce, lazy-seq #'clojure.core/lazy-seq, *print-length* #'clojure.core/*print-length*, *file* #'clojure.core/*file*, compare-and-set! #'clojure.core/compare-and-set!, StackOverflowError java.lang.StackOverflowError, *use-context-classloader* #'clojure.core/*use-context-classloader*, await1 #'clojure.core/await1, let #'clojure.core/let, ref-set #'clojure.core/ref-set, pop-thread-bindings #'clojure.core/pop-thread-bindings, interleave #'clojure.core/interleave, printf #'clojure.core/printf, map? #'clojure.core/map?, -> #'clojure.core/->, defstruct #'clojure.core/defstruct, *err* #'clojure.core/*err*, get #'clojure.core/get, doto #'clojure.core/doto, identity #'clojure.core/identity, into #'clojure.core/into, areduce #'clojure.core/areduce, long #'clojure.core/long, double #'clojure.core/double, volatile? #'clojure.core/volatile?, definline #'clojure.core/definline, Override java.lang.Override, javadoc #'clojure.java.javadoc/javadoc, nfirst #'clojure.core/nfirst, meta #'clojure.core/meta, find-protocol-impl #'clojure.core/find-protocol-impl, bit-and-not #'clojure.core/bit-and-not, *default-data-reader-fn* #'clojure.core/*default-data-reader-fn*, var? #'clojure.core/var?, method-sig #'clojure.core/method-sig, unchecked-add-int #'clojure.core/unchecked-add-int, unquote-splicing #'clojure.core/unquote-splicing, find-doc #'clojure.repl/find-doc, hash-ordered-coll #'clojure.core/hash-ordered-coll, future #'clojure.core/future, IllegalAccessError java.lang.IllegalAccessError, reset-meta! #'clojure.core/reset-meta!, Iterable java.lang.Iterable, Runnable java.lang.Runnable, cycle #'clojure.core/cycle, fn #'clojure.core/fn, seque #'clojure.core/seque, empty? #'clojure.core/empty?, short #'clojure.core/short, CloneNotSupportedException java.lang.CloneNotSupportedException, definterface #'clojure.core/definterface, filterv #'clojure.core/filterv, hash #'clojure.core/hash, quot #'clojure.core/quot, ns-aliases #'clojure.core/ns-aliases, read #'clojure.core/read, unchecked-double #'clojure.core/unchecked-double, NoClassDefFoundError java.lang.NoClassDefFoundError, key #'clojure.core/key, longs #'clojure.core/longs, not= #'clojure.core/not=, string? #'clojure.core/string?, aset-double #'clojure.core/aset-double, unchecked-multiply-int #'clojure.core/unchecked-multiply-int, System java.lang.System, chunk-rest #'clojure.core/chunk-rest, pcalls #'clojure.core/pcalls, *allow-unresolved-vars* #'clojure.core/*allow-unresolved-vars*, remove-all-methods #'clojure.core/remove-all-methods, ns-resolve #'clojure.core/ns-resolve, as-> #'clojure.core/as->, aset-boolean #'clojure.core/aset-boolean, trampoline #'clojure.core/trampoline, NumberFormatException java.lang.NumberFormatException, dir #'clojure.repl/dir, when-not #'clojure.core/when-not, *1 #'clojure.core/*1, vec #'clojure.core/vec, *print-meta* #'clojure.core/*print-meta*, when #'clojure.core/when, int #'clojure.core/int, map-entry? #'clojure.core/map-entry?, ns-refers #'clojure.core/ns-refers, -main #'clojure-noob.core/-main, rand #'clojure.core/rand, second #'clojure.core/second, vector-of #'clojure.core/vector-of, hash-combine #'clojure.core/hash-combine, > #'clojure.core/>, Throwable java.lang.Throwable, replace #'clojure.core/replace, pst #'clojure.repl/pst, associative? #'clojure.core/associative?, unchecked-int #'clojure.core/unchecked-int, set-error-handler! #'clojure.core/set-error-handler!, keyword? #'clojure.core/keyword?, force #'clojure.core/force, bound-fn* #'clojure.core/bound-fn*, namespace-munge #'clojure.core/namespace-munge, group-by #'clojure.core/group-by, IllegalThreadStateException java.lang.IllegalThreadStateException, prn #'clojure.core/prn, extend #'clojure.core/extend, unchecked-multiply #'clojure.core/unchecked-multiply, some->> #'clojure.core/some->>, default-data-readers #'clojure.core/default-data-readers, ->VecSeq #'clojure.core/->VecSeq, even? #'clojure.core/even?, unchecked-dec #'clojure.core/unchecked-dec, tagged-literal? #'clojure.core/tagged-literal?, double-array #'clojure.core/double-array, in-ns #'clojure.core/in-ns, create-ns #'clojure.core/create-ns, re-matcher #'clojure.core/re-matcher, defn #'clojure.core/defn, ref #'clojure.core/ref, NoSuchMethodException java.lang.NoSuchMethodException, bigint #'clojure.core/bigint, extends? #'clojure.core/extends?, promise #'clojure.core/promise, aset-char #'clojure.core/aset-char, rseq #'clojure.core/rseq, Deprecated java.lang.Deprecated, construct-proxy #'clojure.core/construct-proxy, VirtualMachineError java.lang.VirtualMachineError, agent-errors #'clojure.core/agent-errors, *compile-files* #'clojure.core/*compile-files*, *math-context* #'clojure.core/*math-context*, float #'clojure.core/float, pr-str #'clojure.core/pr-str, concat #'clojure.core/concat, aset-short #'clojure.core/aset-short, set-agent-send-off-executor! #'clojure.core/set-agent-send-off-executor!, pp #'clojure.pprint/pp, ArrayIndexOutOfBoundsException java.lang.ArrayIndexOutOfBoundsException, ns #'clojure.core/ns, symbol #'clojure.core/symbol, to-array-2d #'clojure.core/to-array-2d, mod #'clojure.core/mod, amap #'clojure.core/amap, pop #'clojure.core/pop, use #'clojure.core/use, unquote #'clojure.core/unquote, declare #'clojure.core/declare, clojure_noob.core.proxy$java.lang.Object$SignalHandler$d8c00ec7 #'clojure-noob.core/clojure_noob.core.proxy$java.lang.Object$SignalHandler$d8c00ec7, dissoc! #'clojure.core/dissoc!, reductions #'clojure.core/reductions, aset-byte #'clojure.core/aset-byte, ref-history-count #'clojure.core/ref-history-count, - #'clojure.core/-, assoc! #'clojure.core/assoc!, hash-set #'clojure.core/hash-set, ClassNotFoundException java.lang.ClassNotFoundException, reduce-kv #'clojure.core/reduce-kv, or #'clojure.core/or, cast #'clojure.core/cast, InstantiationError java.lang.InstantiationError, ClassFormatError java.lang.ClassFormatError, reset! #'clojure.core/reset!, name #'clojure.core/name, InterruptedException java.lang.InterruptedException, ffirst #'clojure.core/ffirst, sorted-set #'clojure.core/sorted-set, UnsupportedOperationException java.lang.UnsupportedOperationException, counted? #'clojure.core/counted?, RuntimePermission java.lang.RuntimePermission, byte-array #'clojure.core/byte-array, tagged-literal #'clojure.core/tagged-literal, println #'clojure.core/println, extend-type #'clojure.core/extend-type, macroexpand-1 #'clojure.core/macroexpand-1, NoSuchMethodError java.lang.NoSuchMethodError, assoc-in #'clojure.core/assoc-in, char-name-string #'clojure.core/char-name-string, bit-test #'clojure.core/bit-test, defmethod #'clojure.core/defmethod, EMPTY-NODE #'clojure.core/EMPTY-NODE, Thread java.lang.Thread, time #'clojure.core/time, memoize #'clojure.core/memoize, alter-meta! #'clojure.core/alter-meta!, future? #'clojure.core/future?, zero? #'clojure.core/zero?, require #'clojure.core/require, unchecked-dec-int #'clojure.core/unchecked-dec-int, persistent! #'clojure.core/persistent!, nnext #'clojure.core/nnext, StringBuffer java.lang.StringBuffer, add-watch #'clojure.core/add-watch, not-every? #'clojure.core/not-every?, class? #'clojure.core/class?, rem #'clojure.core/rem, agent-error #'clojure.core/agent-error, some #'clojure.core/some, future-cancelled? #'clojure.core/future-cancelled?, memfn #'clojure.core/memfn, struct-map #'clojure.core/struct-map, drop #'clojure.core/drop, *data-readers* #'clojure.core/*data-readers*, nth #'clojure.core/nth, sorted? #'clojure.core/sorted?, nil? #'clojure.core/nil?, extend-protocol #'clojure.core/extend-protocol, split-at #'clojure.core/split-at, *e #'clojure.core/*e, load-reader #'clojure.core/load-reader, Package java.lang.Package, random-sample #'clojure.core/random-sample, cond-> #'clojure.core/cond->, IncompatibleClassChangeError java.lang.IncompatibleClassChangeError, dotimes #'clojure.core/dotimes, UnsatisfiedLinkError java.lang.UnsatisfiedLinkError, select-keys #'clojure.core/select-keys, bit-and #'clojure.core/bit-and, update #'clojure.core/update, list* #'clojure.core/list*, NegativeArraySizeException java.lang.NegativeArraySizeException, reify #'clojure.core/reify, update-in #'clojure.core/update-in, prefer-method #'clojure.core/prefer-method, aset-int #'clojure.core/aset-int, *clojure-version* #'clojure.core/*clojure-version*, ensure-reduced #'clojure.core/ensure-reduced, *' #'clojure.core/*', Readable java.lang.Readable, Boolean java.lang.Boolean, instance? #'clojure.core/instance?, with-open #'clojure.core/with-open, mix-collection-hash #'clojure.core/mix-collection-hash, re-find #'clojure.core/re-find, run! #'clojure.core/run!, BigInteger java.math.BigInteger, val #'clojure.core/val, defonce #'clojure.core/defonce, unchecked-add #'clojure.core/unchecked-add, loaded-libs #'clojure.core/loaded-libs, ->Vec #'clojure.core/->Vec, not #'clojure.core/not, ClassCastException java.lang.ClassCastException, with-meta #'clojure.core/with-meta, unreduced #'clojure.core/unreduced, the-ns #'clojure.core/the-ns, record? #'clojure.core/record?, type #'clojure.core/type, identical? #'clojure.core/identical?, IndexOutOfBoundsException java.lang.IndexOutOfBoundsException, unchecked-divide-int #'clojure.core/unchecked-divide-int, ns-name #'clojure.core/ns-name, max-key #'clojure.core/max-key, *unchecked-math* #'clojure.core/*unchecked-math*, defn- #'clojure.core/defn-, *out* #'clojure.core/*out*, file-seq #'clojure.core/file-seq, source #'clojure.repl/source, agent #'clojure.core/agent, Math java.lang.Math, Thread$UncaughtExceptionHandler java.lang.Thread$UncaughtExceptionHandler, Comparable java.lang.Comparable, ns-map #'clojure.core/ns-map, set-validator! #'clojure.core/set-validator!, Thread$State java.lang.Thread$State, defprotocol #'clojure.core/defprotocol, swap! #'clojure.core/swap!, vals #'clojure.core/vals, unchecked-subtract #'clojure.core/unchecked-subtract, *warn-on-reflection* #'clojure.core/*warn-on-reflection*, sorted-set-by #'clojure.core/sorted-set-by, sync #'clojure.core/sync, assert #'clojure.core/assert, *compile-path* #'clojure.core/*compile-path*, SecurityException java.lang.SecurityException, true? #'clojure.core/true?, Integer java.lang.Integer, release-pending-sends #'clojure.core/release-pending-sends, print #'clojure.core/print, empty #'clojure.core/empty, remove-method #'clojure.core/remove-method, *in* #'clojure.core/*in*, print-ctor #'clojure.core/print-ctor, letfn #'clojure.core/letfn, volatile! #'clojure.core/volatile!, / #'clojure.core//, read-line #'clojure.core/read-line, reader-conditional? #'clojure.core/reader-conditional?, bit-or #'clojure.core/bit-or, clear-agent-errors #'clojure.core/clear-agent-errors, vector #'clojure.core/vector, proxy-super #'clojure.core/proxy-super, >= #'clojure.core/>=, drop-last #'clojure.core/drop-last, not-empty #'clojure.core/not-empty, distinct #'clojure.core/distinct, partition #'clojure.core/partition, Short java.lang.Short, loop #'clojure.core/loop, add-classpath #'clojure.core/add-classpath, bit-flip #'clojure.core/bit-flip, long-array #'clojure.core/long-array, descendants #'clojure.core/descendants, StackTraceElement java.lang.StackTraceElement, merge #'clojure.core/merge, ExceptionInInitializerError java.lang.ExceptionInInitializerError, accessor #'clojure.core/accessor, integer? #'clojure.core/integer?, mapv #'clojure.core/mapv, partition-all #'clojure.core/partition-all, partition-by #'clojure.core/partition-by, numerator #'clojure.core/numerator, object-array #'clojure.core/object-array, with-out-str #'clojure.core/with-out-str, condp #'clojure.core/condp, derive #'clojure.core/derive, Number java.lang.Number, load-string #'clojure.core/load-string, special-symbol? #'clojure.core/special-symbol?, ancestors #'clojure.core/ancestors, subseq #'clojure.core/subseq, error-handler #'clojure.core/error-handler, gensym #'clojure.core/gensym, cond #'clojure.core/cond, TypeNotPresentException java.lang.TypeNotPresentException, ratio? #'clojure.core/ratio?, delay? #'clojure.core/delay?, intern #'clojure.core/intern, print-simple #'clojure.core/print-simple, flatten #'clojure.core/flatten, doubles #'clojure.core/doubles, with-in-str #'clojure.core/with-in-str, remove-watch #'clojure.core/remove-watch, ex-info #'clojure.core/ex-info, ifn? #'clojure.core/ifn?, some-> #'clojure.core/some->, proxy-name #'clojure.core/proxy-name, ns-interns #'clojure.core/ns-interns, all-ns #'clojure.core/all-ns, find-protocol-method #'clojure.core/find-protocol-method, subvec #'clojure.core/subvec, for #'clojure.core/for, binding #'clojure.core/binding, partial #'clojure.core/partial, chunked-seq? #'clojure.core/chunked-seq?, find-keyword #'clojure.core/find-keyword, replicate #'clojure.core/replicate, min-key #'clojure.core/min-key, reduced #'clojure.core/reduced, char-escape-string #'clojure.core/char-escape-string, re-matches #'clojure.core/re-matches, array-map #'clojure.core/array-map, unchecked-byte #'clojure.core/unchecked-byte, with-local-vars #'clojure.core/with-local-vars, ns-imports #'clojure.core/ns-imports, send-off #'clojure.core/send-off, NoSuchFieldError java.lang.NoSuchFieldError, defmacro #'clojure.core/defmacro, every-pred #'clojure.core/every-pred, keys #'clojure.core/keys, Character java.lang.Character, rationalize #'clojure.core/rationalize, load-file #'clojure.core/load-file, distinct? #'clojure.core/distinct?, extenders #'clojure.core/extenders, unchecked-short #'clojure.core/unchecked-short, methods #'clojure.core/methods, odd? #'clojure.core/odd?, ->ArrayChunk #'clojure.core/->ArrayChunk, float-array #'clojure.core/float-array, *3 #'clojure.core/*3, alias #'clojure.core/alias, UnsupportedClassVersionError java.lang.UnsupportedClassVersionError, frequencies #'clojure.core/frequencies, read-string #'clojure.core/read-string, proxy #'clojure.core/proxy, rsubseq #'clojure.core/rsubseq, inc #'clojure.core/inc, get-method #'clojure.core/get-method, with-redefs #'clojure.core/with-redefs, bit-clear #'clojure.core/bit-clear, filter #'clojure.core/filter, locking #'clojure.core/locking, list #'clojure.core/list, + #'clojure.core/+, split-with #'clojure.core/split-with, aset #'clojure.core/aset, ->VecNode #'clojure.core/->VecNode, keyword #'clojure.core/keyword, *ns* #'clojure.core/*ns*, destructure #'clojure.core/destructure, *assert* #'clojure.core/*assert*, defmulti #'clojure.core/defmulti, Exception java.lang.Exception, chars #'clojure.core/chars, str #'clojure.core/str, next #'clojure.core/next, hash-map #'clojure.core/hash-map, if-let #'clojure.core/if-let, underive #'clojure.core/underive, ref-max-history #'clojure.core/ref-max-history, Throwable->map #'clojure.core/Throwable->map, InstantiationException java.lang.InstantiationException, false? #'clojure.core/false?, *print-readably* #'clojure.core/*print-readably*, ints #'clojure.core/ints, class #'clojure.core/class, some-fn #'clojure.core/some-fn, case #'clojure.core/case, *flush-on-newline* #'clojure.core/*flush-on-newline*, to-array #'clojure.core/to-array, bigdec #'clojure.core/bigdec, list? #'clojure.core/list?, bit-not #'clojure.core/bit-not, io! #'clojure.core/io!, xml-seq #'clojure.core/xml-seq, byte #'clojure.core/byte, max #'clojure.core/max, ProcessBuilder java.lang.ProcessBuilder, == #'clojure.core/==, *agent* #'clojure.core/*agent*, lazy-cat #'clojure.core/lazy-cat, comment #'clojure.core/comment, parents #'clojure.core/parents, count #'clojure.core/count, supers #'clojure.core/supers, *fn-loader* #'clojure.core/*fn-loader*, sorted-map-by #'clojure.core/sorted-map-by, BigDecimal java.math.BigDecimal, apply #'clojure.core/apply, IllegalAccessException java.lang.IllegalAccessException, Float java.lang.Float, interpose #'clojure.core/interpose, deref #'clojure.core/deref, assoc #'clojure.core/assoc, rational? #'clojure.core/rational?, transient #'clojure.core/transient, clojure-version #'clojure.core/clojure-version, CharSequence java.lang.CharSequence, chunk-cons #'clojure.core/chunk-cons, comparator #'clojure.core/comparator, SecurityManager java.lang.SecurityManager, StringBuilder java.lang.StringBuilder, sorted-map #'clojure.core/sorted-map, send #'clojure.core/send, drop-while #'clojure.core/drop-while, OutOfMemoryError java.lang.OutOfMemoryError, proxy-call-with-super #'clojure.core/proxy-call-with-super, IllegalMonitorStateException java.lang.IllegalMonitorStateException, realized? #'clojure.core/realized?, char-array #'clojure.core/char-array, resolve #'clojure.core/resolve, compare #'clojure.core/compare, ThreadLocal java.lang.ThreadLocal, complement #'clojure.core/complement, ArithmeticException java.lang.ArithmeticException, *compiler-options* #'clojure.core/*compiler-options*, Long java.lang.Long, *print-dup* #'clojure.core/*print-dup*, defrecord #'clojure.core/defrecord, with-redefs-fn #'clojure.core/with-redefs-fn, sequence #'clojure.core/sequence, constantly #'clojure.core/constantly, get-proxy-class #'clojure.core/get-proxy-class, make-array #'clojure.core/make-array, shorts #'clojure.core/shorts, completing #'clojure.core/completing, update-proxy #'clojure.core/update-proxy, unchecked-negate-int #'clojure.core/unchecked-negate-int, hash-unordered-coll #'clojure.core/hash-unordered-coll, repeat #'clojure.core/repeat, unchecked-inc #'clojure.core/unchecked-inc, IllegalStateException java.lang.IllegalStateException, nthnext #'clojure.core/nthnext, and #'clojure.core/and, create-struct #'clojure.core/create-struct, get-validator #'clojure.core/get-validator, number? #'clojure.core/number?, Compiler clojure.lang.Compiler, RuntimeException java.lang.RuntimeException, await-for #'clojure.core/await-for, chunk-next #'clojure.core/chunk-next, print-str #'clojure.core/print-str, not-any? #'clojure.core/not-any?, into-array #'clojure.core/into-array, SuppressWarnings java.lang.SuppressWarnings, init-proxy #'clojure.core/init-proxy, chunk-buffer #'clojure.core/chunk-buffer, symbol? #'clojure.core/symbol?, Appendable java.lang.Appendable, when-some #'clojure.core/when-some, unchecked-char #'clojure.core/unchecked-char, AssertionError java.lang.AssertionError, Byte java.lang.Byte, ->> #'clojure.core/->>, future-cancel #'clojure.core/future-cancel, var-get #'clojure.core/var-get, commute #'clojure.core/commute, apropos #'clojure.repl/apropos, coll? #'clojure.core/coll?, get-in #'clojure.core/get-in, fnext #'clojure.core/fnext, denominator #'clojure.core/denominator, bytes #'clojure.core/bytes, refer-clojure #'clojure.core/refer-clojure}
;; ```

;; You can use `#'` to grab hold of the var corresponding to the symbol

#'great-books

(deref #'maria.user/great-books)



;; Normally you would just use the symbol:

great-books

(def great-books ["The Power of Bees" "Journey to Upstairs"])

great-books



;; * Updates the var to point to new address of vector

;; * Referred to as `name collision`

;; * Namespaces avoid collisions

;; ### Namespaces

(create-ns 'cheese.taxonomy)



;; Returns a reference to the created namespace object

(ns-name (create-ns 'cheese.taxonomy))



;; Returns the string name of the created namespace object

;; * Probably wont use `create-ns` in code

;; * Using `in-ns` is more common because it creates the ns if it doesn't exist

(in-ns 'cheese.analysis)

*ns*

(in-ns 'cheese.taxonomy)

(def cheddars ["mild" "medium" "strong" "sharp" "extra sharp"])

(in-ns 'cheese.analysis)

cheddars

cheese.taxonomy/cheddars

(in-ns 'cheese.taxonomy)

(def cheddars ["mild" "medium" "strong" "sharp" "extra sharp"])

(def bries ["Wisconsin" "Somerset" "Brie de Meaux" "Brie de Melun"])

(in-ns 'cheese.analysis)

(clojure.core/refer 'cheese.taxonomy)

bries ;; =>  [  "Wisconsin"  "Somerset"  "Brie de Meaux"  "Brie de Melun"]

cheddars ;; =>  ["mild"  "medium"  "strong"  "sharp"  "extra sharp"]



;; > **NOTE:** *Unfortunately the refer command doesn't work in maria.cloud*

;; (clojure.core/get (clojure.core/ns-map clojure.core/*ns*) 'bries)

;; (clojure.core/get (clojure.core/ns-map clojure.core/*ns*) 'cheddars)

;; Hypothetical process:

;; * Calls `ns-interns` on the `cheese.taxonomy` namespace

;; * Merges that with the `ns-map` of the current namespace

;; * Makes the result the new `ns-map` of the current namespace

;; ```
;; (clojure.core/refer 'cheese.taxonomy :only ['bries])
;; cheddars ;; => RuntimeException: Unable to resolve symbol: cheddars
;; bries ;; =>  [  "Wisconsin"  "Somerset"  "Brie de Meaux"  "Brie de Melun"]

;; (clojure.core/refer 'cheese.taxonomy :exclude ['bries])
;; bries ;; => RuntimeException: Unable to resolve symbol: bries
;; cheddars ;; =>  ["mild"  "medium"  "strong"  "sharp"  "extra sharp"]

;; (clojure.core/refer 'cheese.taxonomy :rename {'bries 'yummy-bries})
;; bries ;; => RuntimeException: Unable to resolve symbol: bries
;; yummy-bries ;; => [  "Wisconsin"  "Somerset"  "Brie de Meaux"  "Brie de Melun"]
;; ```

;; Why did we have to use the fully qualified namespace?

;; * The repl automatically refers clojure.core within the user namespace when it loads

;; * Run `(clojure.core/refer-clojure)` after creating a new namespace

(in-ns 'cheese.analysis)

(defn- private-function
  "Just an example function that does nothing"
  [])

(in-ns 'cheese.taxonomy)

(clojure.core/refer-clojure)

(cheese.analysis/private-function)

(clojure.core/refer 'cheese.analysis :only ['private-function])

;; ### Alias

;; ```
;; (clojure.core/alias 'taxonomy 'cheese.taxonomy)
;; taxonomy/bries
;; ["Wisconsin"  "Somerset"  "Brie de Meaux"  "Brie de Melun"]
;; ```



;; Importing with alias, use, and refer
(require 'the-divine-cheese-code.visualization.svg)
(refer 'the-divine-cheese-code.visualization.svg)

;; is equivalent to...
(use 'the-divine-cheese-code.visualization.svg)

;; while this...
(require 'the-divine-cheese-code.visualization.svg)
(refer 'the-divine-cheese-code.visualization.svg)
(alias 'svg 'the-divine-cheese-code.visualization.svg)

;; is equivalent to...
(use '[the-divine-cheese-code.visualization.svg :as svg])
(= svg/points points)
; => true

(= svg/latlng->point latlng->point)
; => true

;; Useful to use both the :as and :refer keywords to expose specific
;; parts of your program
(use '[the-divine-cheese-code.visualization.svg :as svg :only [points]])
(refer 'the-divine-cheese-code.visualization.svg :as :only ['points])
(= svg/points points)
; => true

;; We can use the alias to reach latlng->point
svg/latlng->point
; his doesn't throw an exception

;; But we can't use the bare name
latlng->point
; This does throw an exception!

;; You can control what gets referred from clojure-core with the
;; :refer-clojure keyword
(ns the-divine-cheese-code.core
  (:refer-clojure :exclude [println]))

;; The use, refer, and alias functions are most likely what you will
;; use in the REPL in source files you may use the :require keyword

(ns my.namespace
  (:require [the-divine-cheese-code.visualization.svg :as svg :refer [points]]))

(ns my.namespace
  (:require [the-divine-cheese-code.visualization.svg :refer :all]))


(def available-keywords '(:refer-clojure
                          :require
                          :use
                          :import
                          :load
                          :gen-class))

;; Probably don't need to use the :use keyword

(ns the-divine-cheese-code.core
  (:use clojure.java.browse))

;; is equivalent to...

(in-ns 'the-divine-cheese-code.core)
(use 'clojure.java.browse)

(ns the-divine-cheese-code.core
  (:use [clojure.java browse io]))

;; Use uh uses the first symbol in the vector as a base as a prefix to
;; each symbol that follows

;; is equivalent to...

(in-ns 'the-divine-cheese-code.core)
(use 'clojure.java.browse)
(use 'clojure.java.io)
