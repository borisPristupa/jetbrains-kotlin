package org.jetbrains.dummy.lang.tree

open class RoutedCollectingVisitor<R, in D> : RoutedVisitor<Iterable<R>, D>() {

    override fun emptyResult(): Iterable<R> = emptyList()

    override fun Iterable<Iterable<R>>.collect(data: D): Iterable<R> = flatten()
}